package euopendata.backend.services;

import euopendata.backend.models.HotelHistory;
import euopendata.backend.repositories.HotelHistoryRepository;
import euopendata.backend.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class HotelHistoryService {
    private final HotelHistoryRepository hotelHistoryRepository;

    @Autowired
    public HotelHistoryService(HotelHistoryRepository hotelHistoryRepository) {
        this.hotelHistoryRepository = hotelHistoryRepository;
    }

    public ResponseEntity<?> addHotelToHistory(Integer userId, Integer hotelId) {
        try {
            HotelHistory hotelHistory = new HotelHistory();
            hotelHistory.setUserId(userId);
            hotelHistory.setHotelId(hotelId);
            hotelHistory.setTimestamp(Timestamp.valueOf(java.time.LocalDateTime.now()));
            hotelHistoryRepository.save(hotelHistory);
            return ResponseEntity.ok().body("Hotel added to history successfully.");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Hotel could not be added to history.");
        }
    }
}
