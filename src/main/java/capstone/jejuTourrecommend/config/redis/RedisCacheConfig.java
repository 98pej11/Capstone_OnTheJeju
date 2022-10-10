package capstone.jejuTourrecommend.config.redis;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching // 캐시 사용 설정
public class RedisCacheConfig {

    @Bean(name = "cacheManager")
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues() //null value의 경우 캐시 X
                .entryTtl(Duration.ofMinutes(30)) // 캐시의 기본 유효시간 설정
                .computePrefixWith(CacheKeyPrefix.simple()) //value와 key로 만들어지는 Key값을 ::로 구분
                //캐시 Key를 직렬화-역직렬화 하는데 사용하는 Pair를 지정 -> String으로 지정
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new StringRedisSerializer()))
                //- 캐시 Value를 직렬화-역직렬화 하는데 사용하는 Pair를 지정 -> Value는 다양한 자료구조가 올 수 있으므로 JsonSerializer 사용
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // 캐시키 별 default 유효시간 설정 //내가 만들어준 CacheKey 클래스에서 상수 미리 설정해 놓음
        //(키를 조합할 때 사용하는 Value값, TTL) 형태의 key-value 구조로 캐시 키별 유효시간 설정 가능, put으로 추가 가능
        Map<String, RedisCacheConfiguration> cacheConfiguration = new HashMap<>();
        cacheConfiguration.put(
                CacheKey.ZONE,
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(CacheKey.ZONE_EXPIRE_SEC))
        );


        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(configuration)
                .build();
    }
}
