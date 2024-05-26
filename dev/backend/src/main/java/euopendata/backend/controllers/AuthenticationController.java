package euopendata.backend.controllers;

import euopendata.backend.models.requests.AuthenticationRequest;
import euopendata.backend.models.requests.AuthenticationResponse;
import euopendata.backend.models.requests.RegistrationRequest;
import euopendata.backend.services.AuthenticationService;
import euopendata.backend.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RegistrationService registrationService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
    {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

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
