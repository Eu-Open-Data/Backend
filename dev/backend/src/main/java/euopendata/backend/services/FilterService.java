package euopendata.backend.services;
import euopendata.backend.models.Filter;
import euopendata.backend.repositories.FilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FilterService {
    private final FilterRepository filterRepository;

    @Autowired
    public FilterService(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }

    public List<Filter> getFilters() {
        return filterRepository.getFilters();
    }
}