package euopendata.backend.repositories;

import euopendata.backend.models.HotelHistory;
import euopendata.backend.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelHistoryRepository extends JpaRepository<HotelHistory, Integer> {

    @Query("SELECT l FROM HotelHistory h JOIN Location l ON h.hotelId=l.id WHERE h.userId=:user_id")
    List<Location> getHotelHistoryByUserId(Integer user_id);
}
