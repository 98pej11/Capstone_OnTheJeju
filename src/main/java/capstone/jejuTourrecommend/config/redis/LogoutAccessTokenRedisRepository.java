package capstone.jejuTourrecommend.config.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken,String> {
    // @Indexed 사용한 필드만 가능
    Optional<LogoutAccessToken> findByUsername(String username);
}