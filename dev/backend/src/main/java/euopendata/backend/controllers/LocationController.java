package euopendata.backend.controllers;

import java.util.List;

import euopendata.backend.services.HotelHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import euopendata.backend.models.Location;
import euopendata.backend.services.LocationService;

@RestController
@RequestMapping(path = "/location/")
public class LocationController {
	private final LocationService locationService;
	private final HotelHistoryService hotelHistoryService;
	
	@Autowired 
	public LocationController (LocationService locationService, HotelHistoryService hotelHistoryService) {
		this.locationService = locationService;
        this.hotelHistoryService = hotelHistoryService;
    }
	
	@GetMapping("air/{airQuality}")
	public ResponseEntity<?> filterByAirQuality (@PathVariable String airQuality) {
		return locationService.getByAirQuality(airQuality);
	}
	
	@GetMapping("name/{name}")
	public ResponseEntity<?> filterByName (@PathVariable String name) {
		return locationService.getByName(name);
	}
	
	@GetMapping("soil/{soilQuality}")
	public ResponseEntity<?> filterBySoilQuality (@PathVariable String soilQuality) {
		return locationService.getBySoilQuality(soilQuality);
	}
	
	@GetMapping("type/{type}")
	public ResponseEntity<?> filterByType (@PathVariable String type) {
		return locationService.getByType(type);
	}
	
	@GetMapping("water/{waterQuality}")
	public ResponseEntity<?> filterByWaterQuality (@PathVariable String waterQuality) {
		return locationService.getByWaterQuality(waterQuality);
	}

	@GetMapping("history")
	public ResponseEntity<?> getLocationsHistory(@RequestHeader("Authorization") String authorizationHeader) {
		String token = authorizationHeader.replace("Bearer ", "");
		return hotelHistoryService.getLocationsHistory(token);
	}
}