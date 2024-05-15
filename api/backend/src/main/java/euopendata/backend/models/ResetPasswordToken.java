package euopendata.backend.models;

import euopendata.backend.models.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ResetPasswordToken {
    @SequenceGenerator(
            name = "forgot_password_token_sequence",
            sequenceName = "forgot_password_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,
            generator = "forgot_password_token_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private Users user;

    public ResetPasswordToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, Users user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
