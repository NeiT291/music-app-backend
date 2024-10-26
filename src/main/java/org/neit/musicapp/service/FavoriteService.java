package org.neit.musicapp.service;

import org.neit.musicapp.dto.response.FavoriteResponse;
import org.neit.musicapp.dto.response.SongResponse;
import org.neit.musicapp.entity.Favorite;
import org.neit.musicapp.entity.Song;
import org.neit.musicapp.entity.User;
import org.neit.musicapp.mapper.SongMapper;
import org.neit.musicapp.mapper.UserMapper;
import org.neit.musicapp.repository.FavoriteRepository;
import org.neit.musicapp.repository.SongRepository;
import org.neit.musicapp.repository.UserRepository;
import org.neit.musicapp.utils.TokenInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteService {
    private final TokenInfo tokenInfo;
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserMapper userMapper;
    private final SongMapper songMapper;

    public FavoriteService(TokenInfo tokenInfo, UserRepository userRepository, SongRepository songRepository, FavoriteRepository favoriteRepository, UserMapper userMapper, SongMapper songMapper) {
        this.tokenInfo = tokenInfo;
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.favoriteRepository = favoriteRepository;
        this.userMapper = userMapper;
        this.songMapper = songMapper;
    }

    public void add(Long songId){
        String username = tokenInfo.getUsername();
        if(tokenInfo.getUsername().equals("anonymousUser")){
            throw new RuntimeException("you need to login first");
        }
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("user not found")
        );
        Song song = songRepository.findById(songId).orElseThrow(
                () -> new RuntimeException("song not found")
        );

        if(favoriteRepository.findByUser_IdAndSong_Id(user.getId(), song.getId()) != null){
            return;
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setSong(song);
        favoriteRepository.save(favorite);
    }
    public void remove(Long songId){
        String username = tokenInfo.getUsername();
        if(tokenInfo.getUsername().equals("anonymousUser")){
            throw new RuntimeException("you need to login first");
        }
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("user not found")
        );
        Song song = songRepository.findById(songId).orElseThrow(
                () -> new RuntimeException("song not found")
        );
        Favorite favorite = favoriteRepository.findByUser_IdAndSong_Id(user.getId(), song.getId());
        if(favorite == null){
            return;
        }
        favoriteRepository.delete(favorite);
    }
    public FavoriteResponse getFavorites(String username){
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("user not found")
        );
        List<Favorite> listFavorite = favoriteRepository.findByUser_Username(user.getUsername());

        List<SongResponse> listSong = new ArrayList<>();
        listFavorite.forEach(
                favorite -> listSong.add(songMapper.toSongResponse(favorite.getSong()))
        );

        FavoriteResponse response = new FavoriteResponse();
        response.setUser(userMapper.toUserResponse(user));
        response.setSong(listSong);
        return response;
    }
}
