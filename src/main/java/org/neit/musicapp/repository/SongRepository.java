package org.neit.musicapp.repository;

import org.neit.musicapp.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByNameContainingIgnoreCase(String name);
    List<Song> findByArtist_NicknameContainingIgnoreCase(String username);
}
