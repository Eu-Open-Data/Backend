package euopendata.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import euopendata.backend.models.Photo;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Photo, Integer> {

    List<Photo> findAllByUserId(Integer userId);
}
