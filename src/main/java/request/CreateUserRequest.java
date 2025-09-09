package request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String fname;
    private String lName;
    private String email;
    private String password;
}
