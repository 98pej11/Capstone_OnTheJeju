package capstone.jejuTourrecommend.web.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {

    private int status;
    private Boolean success;
    private String message;
    private UserDto userDto;
    private String accesstoken;


}
