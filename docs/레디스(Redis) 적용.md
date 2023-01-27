
# Redist 데이터 베이스 적용


이전에 accesstoken 정보를 db에 저장하여 관리하였습니다. 
그러면 매번 다른 페이지를 요청할때마다 빈번하게 db에서 접근해야하여 사용자가 많아질경우 비효율적이게 됩니다.

그래서 프로젝트가 종료 후 개선방안을 생각하니 캐시로 데이터를 관리하는 방법을 적용하였습니다. 


## EmbeddedRedisConfig

```
@Configuration  
public class EmbeddedRedisConfig {  
  
   @Value("${spring.redis.port}")  
   private int port;  
  
   @Value("${spring.redis.host}")  
   private String host;  
  
   private RedisServer redisServer;  
  
   @Bean  
   public RedisConnectionFactory redisConnectionFactory() {  
      LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(host, port);  
      return lettuceConnectionFactory;  
   }  
  
   @PostConstruct  
   public void redisServer() throws IOException {  
      int port = isRedisRunning() ? findAvailablePort() : this.port;  
      redisServer = new RedisServer(port);  
      redisServer.start();  
   }  
  
   /**  
    * Embedded Redis가 현재 실행중인지 확인  
    */  
   private boolean isRedisRunning() throws IOException {  
      return isRunning(executeGrepProcessCommand(port));  
   }  
  
   /**  
    * 해당 Process가 현재 실행중인지 확인  
    */  
   private boolean isRunning(Process process) {  
      String line;  
      StringBuilder pidInfo = new StringBuilder();  
  
      try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {  
  
         while ((line = input.readLine()) != null) {  
            pidInfo.append(line);  
         }  
      } catch (Exception e) {  
      }  
  
      return !ObjectUtils.isEmpty(pidInfo.toString());  
   }  
  
   /**  
    * 현재 PC/서버에서 사용가능한 포트 조회  
    */  
   public int findAvailablePort() throws IOException {  
  
      for (int port = 10000; port <= 65535; port++) {  
         Process process = executeGrepProcessCommand(port);  
         if (!isRunning(process)) {  
            return port;  
         }  
      }  
      throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");  
   }  
  
   /**  
    * 해당 port를 사용중인 프로세스 확인하는 sh 실행  
    */  
   private Process executeGrepProcessCommand(int port) throws IOException {  
      String command = String.format("netstat -nat | grep LISTEN|grep %d", port);  
      String[] shell = {"/bin/sh", "-c", command};  
      return Runtime.getRuntime().exec(shell);  
   }  
  
   @PreDestroy  
   public void stopRedis() {  
      if (redisServer != null && redisServer.isActive()) {  
         redisServer.stop();  
      }  
   }  
}
```

##  LogoutAccessToken

```
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

```


##  LogoutAccessTokenRedisRepository

```
public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {  
   // @Indexed 사용한 필드만 가능  
   Optional<LogoutAccessToken> findByUsername(String username);  
}
```

##  RedisCacheConfig

```
@Configuration  
@EnableCaching // 캐시 사용 설정  
public class RedisCacheConfig {  
  
   @Bean(name = "cacheManager")  
   public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {  
      RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()  
         .disableCachingNullValues() //null value의 경우 캐시 X         .entryTtl(Duration.ofMinutes(30)) // 캐시의 기본 유효시간 설정  
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
```