package org.neit.musicapp.mapper;

import org.neit.musicapp.dto.request.UserCreateRequest;
import org.neit.musicapp.dto.response.UserResponse;
import org.neit.musicapp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toUser(UserCreateRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        return user;
    }
    public UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        return response;
    }
}
