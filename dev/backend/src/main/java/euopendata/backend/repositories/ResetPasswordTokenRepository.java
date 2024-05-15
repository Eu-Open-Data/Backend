package euopendata.backend.repositories;

import euopendata.backend.models.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ResetPasswordTokenRepository
        extends JpaRepository<ResetPasswordToken, Long> {

    @Query("SELECT t FROM ResetPasswordToken t WHERE t.token=?1")
    Optional<ResetPasswordToken> findByToken(String resetPasswordToken);
    @Transactional
    @Modifying
    @Query("UPDATE ResetPasswordToken r " +
            "SET r.confirmedAt = ?2 " +
            "WHERE r.token = ?1")
    int updateResetAt(String token, LocalDateTime now);

    void deleteByToken(String token);

    @Modifying
    @Query("DELETE FROM ResetPasswordToken t " +
            "WHERE t.user.email = ?1")
    void deleteByEmail(String email);
}
