package org.neit.musicapp.service;

import org.neit.musicapp.dto.response.SongResponse;
import org.neit.musicapp.entity.Song;
import org.neit.musicapp.entity.User;
import org.neit.musicapp.mapper.SongMapper;
import org.neit.musicapp.repository.SongRepository;
import org.neit.musicapp.repository.UserRepository;
import org.neit.musicapp.utils.TokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.List;

@Service
public class SongService {
    private static final Logger log = LoggerFactory.getLogger(SongService.class);
    private final TokenInfo tokenInfo;
    private final UserRepository userRepository;
    private final String uploadPath = "src/main/resources/upload";
    private final SongRepository songRepository;
    private final SongMapper songMapper;

    public SongService(TokenInfo tokenInfo, UserRepository userRepository, SongRepository songRepository, SongMapper songMapper) {
        this.tokenInfo = tokenInfo;
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.songMapper = songMapper;
    }

    public SongResponse upload(String name,MultipartFile thumbnail, MultipartFile file) throws IOException {
        String username = tokenInfo.getUsername();
        if(tokenInfo.getUsername().equals("anonymousUser")){
            log.error("username is anonymous.");
            throw new RuntimeException("you need to login first");
        }
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("user not found")
        );

        Song song = new Song();
        song.setName(name);
        song.setArtist(user);

        File fileFolder = new File(uploadPath + "/" + user.getUsername());
        if (!fileFolder.exists()){
            fileFolder.mkdirs();
        }
        File uploadFile = new File(fileFolder.getAbsolutePath() + "/"+ file.getOriginalFilename());
        file.transferTo(uploadFile);
        if (!uploadFile.exists() || uploadFile.isDirectory()){
            throw new RemoteException("can't upload file");
        }
        uploadFile = new File(fileFolder.getAbsolutePath() + "/thumb_" + thumbnail.getOriginalFilename());
        thumbnail.transferTo(uploadFile);
        if (!uploadFile.exists() || uploadFile.isDirectory()){
            throw new RemoteException("can't upload file");
        }
        song.setThumbnailPath("thumb_" + thumbnail.getOriginalFilename());
        song.setSongPath(file.getOriginalFilename());

        return songMapper.toSongResponse(songRepository.save(song));

    }
    public List<SongResponse> getAllSongs() {
        return songRepository.findAll().stream().map(songMapper::toSongResponse).toList();
    }
    public List<SongResponse> searchSongsByName(String name) {
        String finalName = String.join(" ", name.split(" ")).trim();
        return songRepository.findByNameContainingIgnoreCase(finalName).stream().map(songMapper::toSongResponse).toList();
    }
    public List<SongResponse> searchSongsByArtist(String nickname) {
        String finalNickname = String.join(" ", nickname.split(" ")).trim();
        return songRepository.findByArtist_NicknameContainingIgnoreCase(finalNickname).stream().map(songMapper::toSongResponse).toList();
    }
    public ResponseEntity<StreamingResponseBody> loadSong(Long songId) throws IOException {
        Song song = songRepository.findById(songId).orElseThrow(
                () -> new RuntimeException("song not found")
        );
        String filePathString = uploadPath + "/" + song.getArtist().getUsername() + "/" + song.getSongPath();
        Path filePath = Paths.get(filePathString);
        if (!filePath.toFile().exists())
        {
            throw new FileNotFoundException("The media file does not exist.");
        }
        long fileSize = Files.size(filePath);
        long endPos = fileSize;
        if (fileSize > 0L)
        {
            endPos = fileSize - 1;
        }
        else
        {
            endPos = 0L;
        }

        return loadPartialMediaFile(filePathString, 0, endPos);

    }
    public ResponseEntity<StreamingResponseBody> loadPartialMediaFile(String localMediaFilePath, long fileStartPos, long fileEndPos) throws IOException {
        StreamingResponseBody responseStream;
        Path filePath = Paths.get(localMediaFilePath);
        if (!filePath.toFile().exists())
        {
            throw new FileNotFoundException("The media file does not exist.");
        }

        long fileSize = Files.size(filePath);
        if (fileStartPos < 0L)
        {
            fileStartPos = 0L;
        }

        if (fileSize > 0L)
        {
            if (fileStartPos >= fileSize)
            {
                fileStartPos = fileSize - 1L;
            }

            if (fileEndPos >= fileSize)
            {
                fileEndPos = fileSize - 1L;
            }
        }
        else
        {
            fileStartPos = 0L;
            fileEndPos = 0L;
        }

        byte[] buffer = new byte[1024];
        String mimeType = Files.probeContentType(filePath);

        final HttpHeaders responseHeaders = new HttpHeaders();
        String contentLength = String.valueOf((fileEndPos - fileStartPos) + 1);
        responseHeaders.add("Content-Type", mimeType);
        responseHeaders.add("Content-Length", contentLength);
        responseHeaders.add("Accept-Ranges", "bytes");
        responseHeaders.add("Content-Range",
                String.format("bytes %d-%d/%d", fileStartPos, fileEndPos, fileSize));

        final long fileStartPos2 = fileStartPos;
        final long fileEndPos2 = fileEndPos;
        responseStream = os -> {
            RandomAccessFile file = new RandomAccessFile(localMediaFilePath, "r");
            try (file)
            {
                long pos = fileStartPos2;
                file.seek(pos);
                while (pos < fileEndPos2)
                {
                    file.read(buffer);
                    os.write(buffer);
                    pos += buffer.length;
                }
                os.flush();
            }
            catch (Exception e) {}
        };

        return new ResponseEntity<StreamingResponseBody>
                (responseStream, responseHeaders, HttpStatus.OK);
    }
    public byte[] getThumbnail(Long id) throws IOException {

        Song song = songRepository.findById(id).orElseThrow(
                () -> new RuntimeException("song not found")
        );

        byte[] image = null;
        try {
            image = Files.readAllBytes(new File(uploadPath + "/" + song.getArtist().getUsername() + "/" + song.getThumbnailPath()).toPath());
        } catch (IOException e) {
            image = Files.readAllBytes(new File("src/main/resources/data/avatar-default.png").toPath());
        }

        return image;
    }
    public SongResponse setThumbnail(Long id, MultipartFile thumbnail) throws IOException {
//      Check user upload song
        String username = tokenInfo.getUsername();
        if(tokenInfo.getUsername().equals("anonymousUser")){
            throw new RuntimeException("you need to login first");
        }
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("user not found")
        );
        Song song = songRepository.findById(id).orElseThrow(
                () -> new RuntimeException("song not found")
        );
        if(!user.equals(song.getArtist())){
            throw new RuntimeException("you don't have upload this song");
        }
//      delete old thumbnail
        File deleteFile = new File(uploadPath + "/" + user.getUsername() + "/" + song.getThumbnailPath());
        deleteFile.delete();
//      Upload new thumbnail
        File fileFolder = new File(uploadPath + "/" + song.getArtist().getUsername());
        if (!fileFolder.exists()){
            fileFolder.mkdirs();
        }
        File uploadFile = new File(fileFolder.getAbsolutePath() + "/thumb_" + thumbnail.getOriginalFilename());
        thumbnail.transferTo(uploadFile);
        if (!uploadFile.exists() || uploadFile.isDirectory()){
            throw new RemoteException("can't upload file");
        }

//      Set new thumbnail path
        song.setThumbnailPath("thumb_" + thumbnail.getOriginalFilename());

        return songMapper.toSongResponse(songRepository.save(song));
    }
    public SongResponse updateName(Long id, String name){
        String username = tokenInfo.getUsername();
        if(tokenInfo.getUsername().equals("anonymousUser")){
            throw new RuntimeException("you need to login first");
        }
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("user not found")
        );
        Song song = songRepository.findById(id).orElseThrow(
                () -> new RuntimeException("song not found")
        );
        if(!user.equals(song.getArtist())){
            throw new RuntimeException("you don't have upload this song");
        }

        song.setName(name);

        return songMapper.toSongResponse(songRepository.save(song));
    }
}
