package org.neit.musicapp.dto.response;

import java.util.List;

public class FavoriteResponse {
    private UserResponse user;
    private List<SongResponse> song;

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public List<SongResponse> getSong() {
        return song;
    }

    public void setSong(List<SongResponse> songs) {
        this.song = songs;
    }
}
