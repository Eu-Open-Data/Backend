package euopendata.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "location")
public class Location {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "air_quality", length = 255, nullable = false)
    private String airQuality;

    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "soil_quality", length = 255, nullable = false)
    private String soilQuality;
    
    @Column(name = "type", length = 255, nullable = false)
    private String type;

    @Column(name = "water_quality", length = 255, nullable = false)
    private String waterQuality;

	public Location() {
		
	}

	public Location(String airQuality, String name, String soilQuality, String type, String waterQuality) {
		this.airQuality = airQuality;
		this.name = name;
		this.soilQuality = soilQuality;
		this.type = type;
		this.waterQuality = waterQuality;
	}
	
	
}
