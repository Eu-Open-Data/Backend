package euopendata.backend.controllers;

import euopendata.backend.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("{id}/firstName")
    public String getFirstNameById(@PathVariable ("id") Long id){
        return usersService.getFirstNameById(id);
    }

    @GetMapping("{id}/lastName")
    public String getLastNameById(@PathVariable ("id") Long id){
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
