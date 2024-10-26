package org.neit.musicapp.dto.response;

import java.time.LocalDate;

public class SongResponse {
    private long id;
    private String name;
    private LocalDate releaseDate;
    private UserResponse artist;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserResponse getArtist() {
        return artist;
    }

    public void setArtist(UserResponse artist) {
        this.artist = artist;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
}
