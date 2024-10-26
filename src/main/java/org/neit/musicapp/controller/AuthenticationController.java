package org.neit.musicapp.controller;
import org.neit.musicapp.dto.ApiResponse;
import org.neit.musicapp.dto.request.UserLoginRequest;
import org.neit.musicapp.dto.response.AuthenticationResponse;
import org.neit.musicapp.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody UserLoginRequest request) {
        return new ApiResponse<>( authenticationService.login(request));
    }
}
