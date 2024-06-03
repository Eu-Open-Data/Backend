package euopendata.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="hotel_history")
public class HotelHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @Column(name="hotel_id", nullable = false)
    private Integer hotelId;
}
