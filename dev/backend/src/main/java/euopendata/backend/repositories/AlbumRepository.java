package euopendata.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import euopendata.backend.models.Photo;

public interface AlbumRepository extends JpaRepository<Photo, Integer> {

}
