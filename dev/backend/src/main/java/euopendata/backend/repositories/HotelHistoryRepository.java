package euopendata.backend.repositories;

import euopendata.backend.models.HotelHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelHistoryRepository extends JpaRepository<HotelHistory, Integer> {
}
