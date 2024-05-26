package euopendata.backend.services;

import euopendata.backend.email.EmailSender;
import euopendata.backend.models.Users;
import euopendata.backend.models.ResetPasswordToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ForgotPasswordService {

    private final UsersService usersService;
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final EmailSender emailSender;

    public void initiatePasswordReset(String email) {
        Users user = usersService.getUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User with email " + email + " does not exist");
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

        ResetPasswordToken resetToken = new ResetPasswordToken(token, createdAt, expiresAt, user);
        resetPasswordTokenService.saveConfirmationToken(resetToken);

        String resetUrl = "https://54.167.96.255:8081/reset-password?token=" + token;
        emailSender.send(email, "Click the following link to reset your password: " + resetUrl);
    }
}
