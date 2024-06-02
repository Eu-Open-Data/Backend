package euopendata.backend.repositories;

import euopendata.backend.models.Photo;
import euopendata.backend.models.Review;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Review r WHERE r.userId = :userId AND r.id = :id")
    void deleteByUserIdAndId(@Param("userId") Integer userId, @Param("id") Integer id);

    List<Review> findAllByUserId(Integer userId);
}

