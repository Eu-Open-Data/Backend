package euopendata.backend.controllers;


import euopendata.backend.models.requests.RegistrationRequest;
import euopendata.backend.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/registration")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request)
    {
        return registrationService.register(request);
    }
    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
