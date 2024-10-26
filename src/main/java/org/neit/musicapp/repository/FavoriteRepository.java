package org.neit.musicapp.repository;

import org.neit.musicapp.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Favorite findByUser_IdAndSong_Id(String userId, Long songId);
    List<Favorite> findByUser_Username(String userId);
}
