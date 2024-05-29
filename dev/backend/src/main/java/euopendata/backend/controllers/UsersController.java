package euopendata.backend.controllers;

import euopendata.backend.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("{token}/firstName")
    public String getFirstNameByToken(@PathVariable ("token") String token){
        return usersService.getFirstNameByToken(token);
    }

    @GetMapping("{token}/lastName")
    public String getLastNameByToken(@PathVariable ("token") String token){
        return usersService.getLastNameByToken(token);
    }

    @GetMapping("{token}/email")
    public String getEmailByToken(@PathVariable ("token") String token){
        return usersService.getEmailByToken(token);
    }

    @GetMapping("{token}/username")
    public String getUsernameByToken(@PathVariable ("token") String token){
        return usersService.getUsernameByToken(token);
    }
}
