package org.neit.musicapp.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate releaseDate = LocalDate.now();
    private String thumbnailPath;
    private String songPath;
    @ManyToOne
    private User artist;

    public Song() {
    }

    public Song(Long id, String name, LocalDate releaseDate, String thumbnailPath, String songPath, User artist) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.thumbnailPath = thumbnailPath;
        this.songPath = songPath;
        this.artist = artist;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public User getArtist() {
        return artist;
    }

    public void setArtist(User artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                ", songPath='" + songPath + '\'' +
                ", artist=" + artist +
                '}';
    }
}
