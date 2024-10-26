package org.neit.musicapp.controller;

import org.neit.musicapp.dto.ApiResponse;
import org.neit.musicapp.dto.response.SongResponse;
import org.neit.musicapp.service.SongService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {
    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping
    public ApiResponse<SongResponse> upload(@RequestParam String name,
                                            @RequestParam MultipartFile thumbnail,
                                            @RequestParam MultipartFile file) throws IOException {
        ApiResponse<SongResponse> response = new ApiResponse<>();
        response.setData(songService.upload(name,thumbnail, file));
        return response;
    }
    @GetMapping
    public ApiResponse<List<SongResponse>> getAll() {
        ApiResponse<List<SongResponse>> response = new ApiResponse<>();
        response.setData(songService.getAllSongs());
        return response;
    }
    @GetMapping("/search")
    public ApiResponse<List<SongResponse>> findByName(@RequestParam String name) {
        ApiResponse<List<SongResponse>> response = new ApiResponse<>();
        response.setData(songService.searchSongsByName(name));
        return response;
    }
    @GetMapping("/artist")
    public ApiResponse<List<SongResponse>> findByArtist(@RequestParam String nickname) {
        ApiResponse<List<SongResponse>> response = new ApiResponse<>();
        response.setData(songService.searchSongsByArtist(nickname));
        return response;
    }
    @GetMapping("/play")
    public ResponseEntity<StreamingResponseBody> loadSong(@RequestParam Long id) throws IOException {
        return songService.loadSong(id);
    }
    @GetMapping("/thumbnail")
    public ResponseEntity<?> downloadThumbnail(@RequestParam Long id) throws IOException {

        byte[] image = songService.getThumbnail(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }
    @PostMapping("/thumbnail")
    public ApiResponse<SongResponse> setThumbnail(@RequestParam Long id, @RequestParam MultipartFile thumbnail) throws IOException {
        ApiResponse<SongResponse> response = new ApiResponse<>();
        response.setData(songService.setThumbnail(id, thumbnail));
        return response;
    }
    @PutMapping("/name")
    public ApiResponse<SongResponse> updateName(@RequestParam Long id, @RequestParam String name) {
        ApiResponse<SongResponse> response = new ApiResponse<>();
        response.setData(songService.updateName(id, name));
        return response;
    }
}
