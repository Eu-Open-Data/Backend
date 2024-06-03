package euopendata.backend.services;

import euopendata.backend.models.Hotel;
import euopendata.backend.models.Photo;
import euopendata.backend.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public ResponseEntity<?> getHotelByName(String name) {
        boolean exists = hotelRepository.existsByName(name);

        if(!exists) {
            return new ResponseEntity<>("Hotel doesn't exist.", HttpStatus.NOT_FOUND);

        }

        return new ResponseEntity<>(hotelRepository.getHotelByName(name), HttpStatus.OK);
    }


    public List<Hotel> getHotelHistory() {
        return hotelRepository.getHotelHistory();
    }

    public ResponseEntity<?> getAllHotels() {
        List<Hotel> hotels = hotelRepository.getAllHotels();

        if (hotels.isEmpty()) {
            return new ResponseEntity<>("No hotels found.", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(hotels, HttpStatus.OK);
        }

    }
    
    public ResponseEntity<?> getHotelsByRating (String rating) {
    	List<Hotel> hotels = hotelRepository.getHotelsByRating(rating);
    	
    	if (hotels.isEmpty()) {
			return new ResponseEntity<>("No hotels found with this rating.", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(hotels, HttpStatus.OK);
		}
    }
}
