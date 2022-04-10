package capstone.jejuTourrecommend.web.login.form;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class JoinForm {

    @NotEmpty(message = "공백을 입력할수 없습니다")
    private String username;

    @NotEmpty(message = "공백을 입력할수 없습니다")
    private String email;

    @NotEmpty(message = "공백을 입력할수 없습니다")
    private String password;

}
