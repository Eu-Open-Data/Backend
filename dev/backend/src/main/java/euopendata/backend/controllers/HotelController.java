package euopendata.backend.controllers;

import euopendata.backend.models.Hotel;
import euopendata.backend.services.HotelHistoryService;
import euopendata.backend.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private HotelHistoryService hotelHistoryService;

    @GetMapping("/{name}")
    public Hotel getHotelByName(@PathVariable String name){
        return hotelService.getHotelByName(name);
    }


    @GetMapping("/history")
    public List<Hotel> getHotelHistory()
    {
        return hotelService.getHotelHistory();
    }

    @PostMapping("/history/{user_id}/{hotel_id}")
    public void addHotelToHistory(@PathVariable Integer user_id, @PathVariable Integer hotel_id)
    {
        hotelHistoryService.addHotelToHistory(user_id, hotel_id);
    }

    @GetMapping("/all")
    public List<Hotel> getAllHotels()
    {
        return hotelService.getAllHotels();
    }


}
