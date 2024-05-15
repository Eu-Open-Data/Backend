package euopendata.backend.services;

import euopendata.backend.models.Location;
import euopendata.backend.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getLocationByName(String name) {
        boolean exists = locationRepository.existsByName(name);

        if(!exists) {
            throw new IllegalArgumentException("Location with name " + name + " not found");
        }

        return locationRepository.getLocationByName(name);
    }

    public List<Location> getLocationByFilter()
    {
        return locationRepository.getLocationByFilter();
    }

    public List<Location> getLocationHistory() {
        return locationRepository.getLocationHistory();
    }
}
