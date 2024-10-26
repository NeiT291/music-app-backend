package org.neit.musicapp.service;

import org.neit.musicapp.dto.request.UserCreateRequest;
import org.neit.musicapp.dto.request.UserUpdateRequest;
import org.neit.musicapp.dto.response.UserResponse;
import org.neit.musicapp.entity.User;
import org.neit.musicapp.mapper.UserMapper;
import org.neit.musicapp.repository.UserRepository;
import org.neit.musicapp.utils.TokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenInfo tokenInfo;
    private final String uplloadPath = "src/main/resources/upload";
    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder, UserRepository userRepository, TokenInfo tokenInfo) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenInfo = tokenInfo;
    }

    public UserResponse register(UserCreateRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public UserResponse updateNickname(UserUpdateRequest request) {
        String username = tokenInfo.getUsername();
        log.info(username);
        if(tokenInfo.getUsername().equals("anonymousUser")){
            log.error("username is anonymous.");
            throw new RuntimeException("you need to login first");
        }
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("user not found")
        );
        user.setNickname(request.getNickname());
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
    public List<UserResponse> searchByNickname(String nickname) {
        String finalNickname = String.join(" ", nickname.split(" ")).trim();
        return userRepository.findByNicknameContainingIgnoreCase(finalNickname).stream().map(userMapper::toUserResponse).toList();
    }
    public void setAvatar(MultipartFile file) throws IOException {
        String username = tokenInfo.getUsername();
        if(tokenInfo.getUsername().equals("anonymousUser")){
            log.error("username is anonymous.");
            throw new RuntimeException("you need to login first");
        }
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("user not found")
        );

        File fileFolder = new File(uplloadPath + "/" + user.getUsername());
        if (!fileFolder.exists()){
            fileFolder.mkdirs();
        }

        File deleteFile = new File(fileFolder.getAbsolutePath() + "/"+ user.getAvatarPath());
        deleteFile.delete();

        File uploadFile = new File(fileFolder.getAbsolutePath() + "/"+ file.getOriginalFilename());
        file.transferTo(uploadFile);
        if (!uploadFile.exists() || uploadFile.isDirectory()){
            throw new RemoteException("can't upload file");
        }

        user.setAvatarPath(file.getOriginalFilename());
        userRepository.save(user);
    }
    public byte[] getAvatar(String username) throws IOException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("user not found"));

        byte[] image = null;
        try {
            image = Files.readAllBytes(new File(uplloadPath + "/" + user.getUsername() + "/" + user.getAvatarPath()).toPath());
        } catch (IOException e) {
            image = Files.readAllBytes(new File("src/main/resources/data/avatar-default.png").toPath());
        }

        return image;
    }
}
