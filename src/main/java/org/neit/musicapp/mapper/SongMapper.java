package org.neit.musicapp.mapper;

import org.neit.musicapp.dto.response.SongResponse;
import org.neit.musicapp.entity.Song;
import org.springframework.stereotype.Component;

@Component
public class SongMapper {
    private final UserMapper userMapper;

    public SongMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public SongResponse toSongResponse(Song song) {
        SongResponse response = new SongResponse();
        response.setId(song.getId());
        response.setName(song.getName());
        response.setArtist(userMapper.toUserResponse(song.getArtist()));
        response.setReleaseDate(song.getReleaseDate());
        return response;
    }
}
