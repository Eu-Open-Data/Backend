package euopendata.backend.controllers;

import euopendata.backend.models.Filter;
import euopendata.backend.services.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/api")
public class FilterController {

    @Autowired
    private FilterService filterService;

    @GetMapping("/filters")
    public List<Filter> getFilters() {
        return filterService.getFilters();
    }
}
