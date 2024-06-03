package euopendata.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import euopendata.backend.models.Location;
import euopendata.backend.services.LocationService;

@RestController
@RequestMapping(path = "/location/")
public class LocationController {
	private final LocationService locationService;
	
	@Autowired 
	public LocationController (LocationService locationService) {
		this.locationService = locationService;
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
}