package euopendata.backend.services;

import euopendata.backend.email.EmailSender;
import euopendata.backend.models.ResetPasswordToken;
import euopendata.backend.models.Users;
import euopendata.backend.models.requests.ConfirmResetPassRequest;
import euopendata.backend.models.requests.ForgotPasswordRequest;
import euopendata.backend.security.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ResetPasswordService {
    private final UsersService usersService;
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final EmailSender emailSender;

    public String processPasswordResetRequest(ForgotPasswordRequest request) {
//        System.out.println(request.getEmail());
        Users user = usersService.getUserByEmail(request.getEmail());
        if (user == null) {
            System.out.println("user not found!");
            throw new IllegalArgumentException("User with email " + request.getEmail() + " not found.");
        }

        String passwordToken = UUID.randomUUID().toString();
        usersService.createResetPasswordToken (user, passwordToken);

        String resetLink = "http://54.167.96.255:8081/reset-password/confirm?token=" + passwordToken;
        emailSender.send(request.getEmail(), "Click the link to reset your password: " + resetLink);
        return resetLink;
    }


    public String verifyPasswordReset(String token) {
        String validateTokenRes = resetPasswordTokenService.validateResetPasswordToken(token);
        if (!validateTokenRes.equals("token valid"))
            return validateTokenRes;

//        Optional<ResetPasswordToken> resetToken = resetPasswordTokenService.getToken(token);
//        Users user = resetToken.get().getUser();
//        PasswordEncoder passwordEncoder = new PasswordEncoder(newPassword);
//        user.setPassword(passwordEncoder.getPassword());
//        user.setEnabled(true);
//        usersService.updateUser(user);
//
//        resetPasswordTokenService.deleteToken(token); 
        return token;
    }
    
    public String resetPassword (String token, ConfirmResetPassRequest request) {
    	String newPassword = request.getNewPassword();
    	String confirmPassword = request.getConfirmPassword();
    	if (!newPassword.equals(confirmPassword))
    		return "Passwords don't match!";
    	
    	Optional<ResetPasswordToken> resetToken = resetPasswordTokenService.getToken(token);
        Users user = resetToken.get().getUser();
        PasswordEncoder passwordEncoder = new PasswordEncoder(newPassword);
        user.setPassword(passwordEncoder.getPassword());
        user.setEnabled(true);
        usersService.updateUser(user);

        resetPasswordTokenService.deleteToken(token); 
    	
    	return "Password reset successfully.";
    }

}
