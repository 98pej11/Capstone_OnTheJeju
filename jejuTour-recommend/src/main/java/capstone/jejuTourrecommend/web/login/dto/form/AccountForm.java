package capstone.jejuTourrecommend.web.login.dto.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class AccountForm {

    private String email;


    private String password;

}