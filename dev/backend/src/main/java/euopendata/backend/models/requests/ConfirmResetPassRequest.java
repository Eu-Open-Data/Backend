package euopendata.backend.models.requests;

import lombok.Data;

@Data
public class ConfirmResetPassRequest {
	private String password;
}
