package euopendata.backend.models.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AuthenticationRequest {

    private final String username;
    private final String password;
}
