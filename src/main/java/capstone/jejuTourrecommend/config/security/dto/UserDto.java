package capstone.jejuTourrecommend.config.security.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

	private Long id;
	private String username;
	private String email;
	private String role;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd | HH:mm:ss")
	private LocalDateTime createdDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd | HH:mm:ss")
	private LocalDateTime lastModifiedDate;

}










