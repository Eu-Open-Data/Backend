package euopendata.backend.repositories;

import euopendata.backend.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {

    @Query
    boolean existsByName(String name);

    @Query("SELECT h FROM Hotel h WHERE h.name = :name")
    Hotel getHotelByName(String name);

    @Query(value = "SELECT * FROM hotels LIMIT 10", nativeQuery = true)
    List<Hotel> getHotelHistory();

    @Query(value = "SELECT * FROM hotels LIMIT 30", nativeQuery = true)
    List<Hotel> getAllHotels();
    
    @Query ("SELECT h FROM Hotel h WHERE h.rating = :rating")
    List<Hotel> getHotelsByRating (String rating);
}
