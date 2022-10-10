package capstone.jejuTourrecommend.web.login.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDto {

//        "user": {
//            "id": 0,
//                    "name": "김팀블",
//                    "email": "teamble@gmail.com",
//                    "createdAt": "2021-11-37T01:06:14.2472",  *****
//            "updatedAt": "2021-11-37T01:06:14.2472",*****
//            "isDeleted": false,  *****
//        },

    private Long id;
    private String username;
    private String email;
    private String role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd | HH:mm:ss")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd | HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    //refreshToken;
    //private String refreshToken;


}










