package capstone.jejuTourrecommend.config.redis;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
	// @Indexed 사용한 필드만 가능
	Optional<LogoutAccessToken> findByUsername(String username);
}
