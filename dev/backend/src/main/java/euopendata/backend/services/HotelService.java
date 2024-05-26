package euopendata.backend.services;

import euopendata.backend.models.Hotel;
import euopendata.backend.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Hotel getHotelByName(String name) {
        boolean exists = hotelRepository.existsByName(name);

        if(!exists) {
            throw new IllegalArgumentException("Hotel with name " + name + " not found");
        }

        return hotelRepository.getHotelByName(name);
    }


    public List<Hotel> getHotelHistory() {
        return hotelRepository.getHotelHistory();
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.getAllHotels();
    }
}
