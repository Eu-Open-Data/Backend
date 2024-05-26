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
    public String getFirstNameById(@PathVariable ("token") Long id){
        return usersService.getFirstNameById(id);
    }

    @GetMapping("{token}/lastName")
    public String getLastNameById(@PathVariable ("token") Long id){
        return usersService.getLastNameById(id);
    }

    @GetMapping("{token}/email")
    public String getEmailById(@PathVariable ("token") Long id){
        return usersService.getEmailById(id);
    }

    @GetMapping("{token}/username")
    public String getUsernameById(@PathVariable ("token") Long id){
        return usersService.getUsernameById(id);
    }
}
