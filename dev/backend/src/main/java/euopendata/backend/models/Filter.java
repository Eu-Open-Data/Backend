package euopendata.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    @ElementCollection
    @CollectionTable(name = "filter_options", joinColumns = @JoinColumn(name = "filter_id"))
    @Column(name = "option")
    private List<String> options;


    public Filter(String name, String type, List<String> options) {
        this.name = name;
        this.type = type;
        this.options = options;
    }
}
