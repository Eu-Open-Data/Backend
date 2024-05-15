package euopendata.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @SequenceGenerator(
            name="confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize=1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,
                    generator="confirmation_token_sequence"
    )
    private Long id;
    @Column(nullable= false)
    private String token;
    @Column(nullable= false)
    private LocalDateTime createdAt;
    @Column(nullable= false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name="user_id"
    )
    private Users user;

    public ConfirmationToken(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiredAt,
                             Users user
    ) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiredAt;
        this.confirmedAt = confirmedAt;
        this.user = user;
    }
}
