package euopendata.backend.repositories;

import euopendata.backend.models.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilterRepository extends JpaRepository<Filter,Long> {

    @Query("SELECT f FROM Filter f")
    List<Filter> getFilters();
}
