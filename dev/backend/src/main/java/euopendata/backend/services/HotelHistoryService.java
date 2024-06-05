package euopendata.backend.services;

import euopendata.backend.models.HotelHistory;
import euopendata.backend.models.Location;
import euopendata.backend.repositories.HotelHistoryRepository;
import euopendata.backend.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class HotelHistoryService {
    private final HotelHistoryRepository hotelHistoryRepository;
    private final JwtService jwtService;
    private final UsersService usersService;

    @Autowired
    public HotelHistoryService(HotelHistoryRepository hotelHistoryRepository, JwtService jwtService, UsersService usersService) {
        this.hotelHistoryRepository = hotelHistoryRepository;
        this.jwtService = jwtService;
        this.usersService = usersService;
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

    public ResponseEntity<?> getLocationsHistory(String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());
        List<Location> locations = hotelHistoryRepository.getHotelHistoryByUserId(userId);

        if(locations.isEmpty()) {
            return new ResponseEntity<>("No history found for the user.", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(locations, HttpStatus.OK);
        }
    }
}
