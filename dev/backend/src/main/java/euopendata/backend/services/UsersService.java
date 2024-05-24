package euopendata.backend.services;

import euopendata.backend.models.Users;
import euopendata.backend.repositories.UsersRepository;
import euopendata.backend.security.PasswordEncoder;
import euopendata.backend.models.ConfirmationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.status;

@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with email %s not found";
    @Autowired
    private final UsersRepository usersRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final ResetPasswordTokenService resetPasswordTokenService;

//    @Autowired
//    public UsersService(UsersRepository usersRepository, ConfirmationTokenService confirmationTokenService) {
//        this.usersRepository = usersRepository;
//        this.confirmationTokenService = confirmationTokenService;
//    }
//    @Autowired
//    public UsersService(ResetPasswordTokenService resetPasswordTokenService) {
//        this.resetPasswordTokenService = resetPasswordTokenService;
//    }

    public String getEmailById(Long id) {
        boolean exists = usersRepository.existsById(id);
        if(!exists) {
            throw new IllegalStateException("user with id " + id + " does not exist");
        }
        return usersRepository.getEmailById(id);
    }

    public String getFirstNameById(Long id) {
        boolean exists = usersRepository.existsById(id);
        if(!exists) {
            throw new IllegalStateException("user with id " + id + " does not exist");
        }
        return usersRepository.getFirstNameById(id);
    }

    public String getLastNameById(Long id) {
        boolean exists = usersRepository.existsById(id);
        if(!exists) {
            throw new IllegalStateException("user with id " + id + " does not exist");
        }
        return usersRepository.getLastNameById(id);
    }

    public String getUsernameById(Long id) {
        boolean exists = usersRepository.existsById(id);
        if(!exists) {
            throw new IllegalStateException("user with id " + id + " does not exist");
        }
        return usersRepository.getUsernameById(id);
    }

    public Users getUserByEmail(String email) {
        return usersRepository.findByEmail(email).orElse(null);
    }

    public void updateUser(Users user) {
        usersRepository.save(user);
    }

    public ResponseEntity<String> signUpUser(Users user)
    {
        boolean userExists = usersRepository.findByEmail(user.getEmail()).isPresent();
        if(userExists){
            return ResponseEntity.badRequest().body("Email already used!");
        }

        boolean usernameExists = usersRepository.findByUsername(user.getUsername()).isPresent();
        if(usernameExists){
            return ResponseEntity.badRequest().body("Username already used!");
        }

        PasswordEncoder passwordEncoder = new PasswordEncoder(user.getPassword());

        user.setPassword(passwordEncoder.getPassword());
        usersRepository.save(user);

        System.out.println(
                "user saved"
        );

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken= new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(USER_NOT_FOUND_MSG, username)
                ));
    }

    public int enableUser(String email) {
        return usersRepository.enableAppUser(email);
    }

    public void createResetPasswordToken(Users user, String passwordToken) {
        resetPasswordTokenService.createResetPasswordTokenForUser(user, passwordToken);
    }
}
