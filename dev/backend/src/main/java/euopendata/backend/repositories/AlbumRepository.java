package euopendata.backend.repositories;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import euopendata.backend.models.Photo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Photo, Integer> {

    List<Photo> findAllByUserId(Integer userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Photo p WHERE p.userId = :userId AND p.id = :id")
    void deleteByUserIdAndId(@Param("userId") Integer userId, @Param("id") Integer id);

}

