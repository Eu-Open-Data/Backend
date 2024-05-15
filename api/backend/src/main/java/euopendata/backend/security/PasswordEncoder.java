package euopendata.backend.security;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

    private final String password;

    public PasswordEncoder(String password)
    {
        this.password = encodePassword(password);
    }

    private String encodePassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public String getPassword() {
        return password;
    }
}
