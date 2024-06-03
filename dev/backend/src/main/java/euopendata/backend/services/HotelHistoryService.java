package euopendata.backend.services;

import euopendata.backend.models.HotelHistory;
import euopendata.backend.repositories.HotelHistoryRepository;
import euopendata.backend.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelHistoryService {
    private final HotelHistoryRepository hotelHistoryRepository;

    @Autowired
    public HotelHistoryService(HotelHistoryRepository hotelHistoryRepository) {
        this.hotelHistoryRepository = hotelHistoryRepository;
    }

    public void addHotelToHistory(Integer userId, Integer hotelId) {
            HotelHistory hotelHistory = new HotelHistory();
            hotelHistory.setUserId(userId);
            hotelHistory.setHotelId(hotelId);
            hotelHistoryRepository.save(hotelHistory);
    }
}
