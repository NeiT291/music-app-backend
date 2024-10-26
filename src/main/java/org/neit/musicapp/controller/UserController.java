package org.neit.musicapp.controller;

import org.neit.musicapp.dto.ApiResponse;
import org.neit.musicapp.dto.request.UserCreateRequest;
import org.neit.musicapp.dto.request.UserUpdateRequest;
import org.neit.musicapp.dto.response.UserResponse;
import org.neit.musicapp.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ApiResponse<UserResponse> create(@RequestBody UserCreateRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setData(userService.register(request));
        return response;
    }
    @PutMapping
    public ApiResponse<UserResponse> updateNickname(@RequestBody UserUpdateRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setData(userService.updateNickname(request));
        return response;
    }
    @GetMapping
    public ApiResponse<List<UserResponse>> getAll() {
        ApiResponse<List<UserResponse>> response = new ApiResponse<>();
        response.setData(userService.getAll());
        return response;
    }
    @GetMapping("/search")
    public ApiResponse<List<UserResponse>> search(@RequestParam String nickname) {
        ApiResponse<List<UserResponse>> response = new ApiResponse<>();
        response.setData(userService.searchByNickname(nickname));
        return response;
    }
    @PostMapping("/avatar")
    public ApiResponse<?> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        ApiResponse<?> response = new ApiResponse<>();
        userService.setAvatar(file);
        return response;
    }
    @GetMapping("/avatar")
    public ResponseEntity<?> downloadAvatar(@RequestParam("username") String username) throws IOException {

        byte[] image = userService.getAvatar(username);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }
}
