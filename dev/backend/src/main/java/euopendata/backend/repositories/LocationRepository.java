package euopendata.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import euopendata.backend.models.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {

	@Query("SELECT l FROM Location l WHERE l.airQuality = :airQuality")
	List<Location> findByAirQuality (String airQuality);

	@Query("SELECT l FROM Location l WHERE l.name = :name")
	List<Location> findByName (String name);

	@Query("SELECT l FROM Location l WHERE l.soilQuality = :soilQuality")
	List<Location> findBySoilQuality (String soilQuality);

	@Query("SELECT l FROM Location l WHERE l.type = :type")
	List<Location> findByType (String type);

	@Query("SELECT l FROM Location l WHERE l.waterQuality = :waterQuality")
	List<Location> findByWaterQuality (String waterQuality);
}
