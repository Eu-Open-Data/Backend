package euopendata.backend.services;

import euopendata.backend.models.ResetPasswordToken;
import euopendata.backend.models.Users;
import euopendata.backend.repositories.ResetPasswordTokenRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rds.endpoints.internal.Value;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor
public class ResetPasswordTokenService {
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    public void createResetPasswordTokenForUser (Users user, String password) {
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(password,
                                                                        LocalDateTime.now(),
                                                                        LocalDateTime.now().plusMinutes(10),
                                                                        user);
        resetPasswordTokenRepository.save(resetPasswordToken);
    }

    public String validateResetPasswordToken (String token) {
        Optional<ResetPasswordToken> resetPasswordToken = resetPasswordTokenRepository.findByToken(token);
        if (resetPasswordToken.isEmpty())
            return "Invalid reset password token";

        Users user = resetPasswordToken.get().getUser();
        if (resetPasswordToken.get().getExpiresAt().isAfter(LocalDateTime.now()))
            return "Your reset password link has expired";
        return "token valid";
    }

    public Optional<Users> findUserByResetPwdToken (String resetPasswordToken) {
        return Optional.ofNullable(resetPasswordTokenRepository
                .findByToken(resetPasswordToken).get().getUser());
    }

    public void saveConfirmationToken(ResetPasswordToken resetPasswordToken){
        resetPasswordTokenRepository.save(resetPasswordToken);
    }

    public Optional<ResetPasswordToken> getToken(String token) {
        return resetPasswordTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return resetPasswordTokenRepository.updateResetAt(
                token, LocalDateTime.now());
    }

    public void deleteToken(String token) {
        resetPasswordTokenRepository.deleteByToken(token);
    }

    public void deleteTokenByEmail(String email) {
        resetPasswordTokenRepository.deleteByEmail(email);
    }
}

