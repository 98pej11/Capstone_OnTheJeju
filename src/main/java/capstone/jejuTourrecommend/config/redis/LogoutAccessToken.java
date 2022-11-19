package capstone.jejuTourrecommend.config.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Getter
@RedisHash("logoutAccessToken")
@AllArgsConstructor
@Builder
public class LogoutAccessToken {

	@Id
	private String id;

	@Indexed // 필드 값으로 데이터 찾을 수 있게 하는 어노테이션(findByAccessToken)
	private String userEmail;

	@TimeToLive
	private Long expiration; // seconds

	public static LogoutAccessToken createLogoutAccessToken(String accessToken, String username,
															Long remainingMilliSeconds) {
		return LogoutAccessToken.builder()
			.id(accessToken)
			.userEmail(username)
			.expiration(remainingMilliSeconds / 1000)
			.build();
	}

}



