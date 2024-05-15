package euopendata.backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Location{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String air_quality;

    @Column(nullable = false)
    private String soil_quality;

    @Column(nullable = false)
    private String water_quality;


    public Location(String name, String type, String air_quality, String soil_quality, String water_quality){
        this.name = name;
        this.type = type;
        this.air_quality = air_quality;
        this.soil_quality = soil_quality;
        this.water_quality = water_quality;
    }


}
