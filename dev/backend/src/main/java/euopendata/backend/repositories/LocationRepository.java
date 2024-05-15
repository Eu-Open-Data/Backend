package euopendata.backend.repositories;

import euopendata.backend.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {

    @Query
    boolean existsByName(String name);

    @Query
    Location getLocationByName(String name);

    @Query(value = "SELECT * FROM location LIMIT 20", nativeQuery = true)
    List<Location> getLocationByFilter();

    @Query(value = "SELECT * FROM location LIMIT 10", nativeQuery = true)
    List<Location> getLocationHistory();
}
