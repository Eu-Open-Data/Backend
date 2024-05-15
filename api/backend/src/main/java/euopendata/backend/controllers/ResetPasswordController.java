package euopendata.backend.controllers;

import euopendata.backend.models.Users;
import euopendata.backend.models.requests.ForgotPasswordRequest;
import euopendata.backend.services.ResetPasswordService;
import euopendata.backend.services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/reset-password")
@AllArgsConstructor
public class ResetPasswordController {
    private final ResetPasswordService resetPasswordService;

    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody ForgotPasswordRequest request) {
        System.out.println("in controller!");

        String resetPasswordUrl =
                resetPasswordService.processPasswordResetRequest(request);
        return ResponseEntity.ok("Password reset email sent successfully.");
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmResetToken(@RequestParam("token") String token, @RequestParam("password") String password) {
        String response =
                resetPasswordService.confirmPasswordReset(token, password);
        return ResponseEntity.ok(response);
    }
}