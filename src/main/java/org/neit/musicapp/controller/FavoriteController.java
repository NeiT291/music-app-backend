package org.neit.musicapp.controller;

import org.neit.musicapp.dto.ApiResponse;
import org.neit.musicapp.dto.response.FavoriteResponse;
import org.neit.musicapp.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ApiResponse<?> addFavorite(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        favoriteService.add(id);
        return response;
    }
    @DeleteMapping
    public ApiResponse<?> removeFavorite(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        favoriteService.remove(id);
        return response;
    }
    @GetMapping
    public ApiResponse<FavoriteResponse> getFavorite(@RequestParam String username) {
        ApiResponse<FavoriteResponse> response = new ApiResponse<>();
        response.setData(favoriteService.getFavorites(username));
        return response;
    }
}
