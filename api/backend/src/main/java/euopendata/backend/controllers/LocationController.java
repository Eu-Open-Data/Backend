package euopendata.backend.controllers;

import euopendata.backend.models.Location;
import euopendata.backend.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/{name}")
    public Location getLocationByName(@PathVariable String name){
        return locationService.getLocationByName(name);
    }

    @GetMapping("/search-by-filters")
    public List<Location> getLocationByFilter()
    {
        return locationService.getLocationByFilter();
    }

    @GetMapping("/history")
    public List<Location> getLocationHistory()
    {
        return locationService.getLocationHistory();
    }


}
