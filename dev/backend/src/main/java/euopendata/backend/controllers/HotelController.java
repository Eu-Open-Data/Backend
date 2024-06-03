package euopendata.backend.controllers;

import euopendata.backend.models.Hotel;
import euopendata.backend.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/{name}")
    public ResponseEntity<?> getHotelByName(@PathVariable String name){
        return hotelService.getHotelByName(name);
    }


    @GetMapping("/history")
    public List<Hotel> getHotelHistory()
    {
        return hotelService.getHotelHistory();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllHotels()
    {
        return hotelService.getAllHotels();
    }

    @GetMapping("/rating/{rating}")
    public ResponseEntity<?> getHotelsByRating (@PathVariable String rating) {
    	return hotelService.getHotelsByRating(rating);
    }

}
