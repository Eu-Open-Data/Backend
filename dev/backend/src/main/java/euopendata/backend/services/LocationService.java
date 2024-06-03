package euopendata.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import euopendata.backend.models.Location;
import euopendata.backend.repositories.LocationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class LocationService {
	private final LocationRepository locationRepository;
	private final JwtService jwtService;
	private final UsersService usersService;

	@Autowired
	public LocationService (LocationRepository locationRepository, JwtService jwtService, UsersService usersService) {
		this.locationRepository = locationRepository;
        this.jwtService = jwtService;
        this.usersService = usersService;
    }

	public ResponseEntity<?> getByAirQuality (String airQuality) {
		List<Location> locations = locationRepository.findByAirQuality(airQuality);
		
		if (locations.isEmpty()) {
			return new ResponseEntity<>("No locations found with this air quality", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(locations, HttpStatus.OK);
		}
	}

	public ResponseEntity<?> getByName (String name) {
		List<Location> locations = locationRepository.findByName(name);
		
		if (locations.isEmpty()) {
			return new ResponseEntity<>("No locations found with this name", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(locations, HttpStatus.OK);
		}
	}

	public ResponseEntity<?> getBySoilQuality (String soilQuality) {
		List<Location> locations = locationRepository.findBySoilQuality(soilQuality);
		
		if (locations.isEmpty()) {
			return new ResponseEntity<>("No locations found with this soil quality", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(locations, HttpStatus.OK);
		}
	}

	public ResponseEntity<?> getByType (String type) {
		List<Location> locations = locationRepository.findByType(type);
		
		if (locations.isEmpty()) {
			return new ResponseEntity<>("No locations found with this type", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(locations, HttpStatus.OK);
		}
	}

	public ResponseEntity<?> getByWaterQuality (String waterQuality) {
		List<Location> locations = locationRepository.findByWaterQuality(waterQuality);
		
		if (locations.isEmpty()) {
			return new ResponseEntity<>("No locations found with this water quality", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(locations, HttpStatus.OK);
		}
	}
}
