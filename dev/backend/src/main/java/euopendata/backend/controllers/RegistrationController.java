package euopendata.backend.controllers;


import euopendata.backend.models.requests.RegistrationRequest;
import euopendata.backend.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(path = "/registration")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) throws UnsupportedEncodingException {
        return registrationService.register(request);
    }
    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
    @GetMapping(path = "/resend/{id}")
    public String resendEmailConfirmation(@PathVariable ("id") Long id) {
        return registrationService.resendEmailConfirmation(id);
    }


}
