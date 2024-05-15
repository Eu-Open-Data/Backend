package euopendata.backend.models.requests;

import lombok.*;

//@Getter
//@AllArgsConstructor
//@EqualsAndHashCode
//@ToString
@Data
public class ForgotPasswordRequest {
    private String email;
    private String newPassword;
}
