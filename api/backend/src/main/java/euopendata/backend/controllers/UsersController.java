package euopendata.backend.controllers;

import euopendata.backend.services.UsersService;
import euopendata.backend.models.Users;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @GetMapping("/username")
    public String getCurrentUsername() {
        return usersService.getUsernameLogged();
    }

    @GetMapping("/email")
    public String getCurrentEmail() {
        return usersService.getEmailLogged();
    }

    @GetMapping("{id}/firstName")
    public String getFirstNameById(@PathVariable ("id") Long id) {
        return usersService.getFirstNameById(id);
    }
    @GetMapping("{id}/lastName")
    public String getLastNameById(@PathVariable ("id") Long id) {
        return usersService.getLastNameById(id);
    }
    @GetMapping("{id}/email")
    public String getEmailById(@PathVariable ("id") Long id){
        return usersService.getEmailById(id);
    }

    @GetMapping("{id}/username")
    public String getUsernameById(@PathVariable ("id") Long id){
        return usersService.getUsernameById(id);
    }

}
