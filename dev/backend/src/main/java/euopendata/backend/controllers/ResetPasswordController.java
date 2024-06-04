package euopendata.backend.controllers;

import euopendata.backend.models.Users;
import euopendata.backend.models.requests.ConfirmResetPassRequest;
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
    	String resetPasswordUrl =
                resetPasswordService.processPasswordResetRequest(request);
        return ResponseEntity.ok("Password reset email sent successfully.");
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> verifyResetToken(@RequestParam("token") String token) {
        String response =
                resetPasswordService.verifyPasswordReset(token);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/reset")
    public ResponseEntity<String> resetPass (@RequestParam("token") String token, @RequestBody ConfirmResetPassRequest request) {
        String response =
                resetPasswordService.resetPassword(token, request);
        return ResponseEntity.ok(response);
    }
}
