

# 세션 기반이 아닌 JWT 토큰 기반 인증/인가 처리 이유


원래 초창기에는  세션 기반으로 사용자 인증 처리를 하려 하였습니다. 그러나 세션 기반의 로그인 처리는 다음과 같은 문제점이 있었습니다  

1. 저장공간의 용량
	- 세션은 서버의 메모리 내부에 저장됩니다.  유저가 많아지게 되면 세션의 양도 많아져 메모리에 부화가 걸리게 됩니다
2. 빈번한 접근
	- 인증 처리는 매번 다른 페이지로 접근시 사용자 정보를 확인해야합니다. 이러한 빈번한 인증 처리는 데이터베이스 자원 접근의 지나친 사용으로 부하가 걸리게 됩니다.
3. 확장성 문제
	-  세션은 기본적으로 상태유지(stateful)하여 항상 같은 서버가 유지되어야 합니다. 
	- 서비스 규모가 커져서 서버를 여러대로 확장, 분산하게 된다면 세션 또한 분산 시켜야 합니다. 그래서 서버가 여러대을 분산시키는 기술을 따로 설계해야합니다.
	

jwt 토근 인증/ 인가 처리는 위와같은 문제들을 해결 해줍니다

1. 저장공간의 용량, 빈번한 접근
	- redis 로 토큰 정보를 저장하므로, 메모리 내부가 아닌 캐시를 사용하여 세션의 단점을 해결하였습니다

2.  확장성 문제
	- jwt는 무상태(stateless)이므로 항상 같은 서버를 유지할 필요가 없습니다.
쿠키에 저장할 때 `http-only` 를 사용한다면 HTTPS로만 쿠키가 전달되기 때문에 보안을 강화할 수 있고, CSRF 문제에 대해서는 CSURF같은 라이브러리를 사용함으로 해결할 수 있다.

세션 말고 쿠키는 더 위험하다. 쿠키는 브라우저에서 임의로 바꿀수 있게 때문에 쿠키를 임의로 변경하여 다른 사용자 정보도 접근할 수도 있다.

JWT를 서버가 발급하고나면 JWT를 관리하지 않는다. 오직 JWT를 받았을 때 JWT가 유효한 것인지만 확인하기 때문에 서버 자원과 비용을 절감할 수 있다. 그러나 JWT가 수명이 길어서 제 3자에게 탈취당할 경우가 발생할 수 있다. 이를 보완한 방법 중 하나로 access token & refresh token 방식이 있는데 두개의 토큰 모두 JWT이다.

아니면 버전 정보를 jwt 넣으면 괜찮을 것같다
	- 즉 고정된 정보가 아닌 동적이 적보를 jwt 에 넣는 방법으로 ㅎ

# 스프링 Security 개선 사항

원래 기존 spring security에서는 filter내에서 accesstoken의 유효성검증과 사용자권한등 모두 한 filter에서 처리하였다. 이후 spring security를 학습하고 인증, 인가 괄련 역할을 한곳에 모으지 않고 역할과 책임을 분리하여 패키지를 분리하였고 그에 따라 세부적으로 객체도 분리하였습니다.


1.   filter (JwtAuthenticationFilter, LoginProcessingAuthenticationFilter)
2.   handler(LoginSuccessHandler, LoginFailureHandler, RestAccessDeniedHandler, RestAuthenticationEntryPoint)
3.  provider(JwtTokenProvider, CustomAuthenticationProvider)를 만들어, security 라이브러리 클래스 목적에 맞게 implement 하여 재정의하였습니다.



## 1. Filter 패키지

filter 패키지에서는 크개 2가지 역할로 나누었습니다. 
1. JwtAuthenticationFilter 
	- 웹서버에서 웹 어플리케이션 서버(was)로 accesstoken이 들어올 때, 해당 accesstoken이 유요한지 판다는 필터입니다
2. LoginProcessingAuthenticationFilter
	- 사용자가 로그인 요청을 할 때, 올라른 로그인 정보를 가졌는지 확인하는 필터입니다.


### (1) JwtAuthenticationFilter
OncePerRequestFilter 상속받아 커스텀화 하였습니다
JwtAuthenticationFilter 필터는 웹서버에서 웹 어플리케이션 서버(was)로 accesstoken이 들어올 때, 해당 accesstoken이 유효한지 판단하는 필터입니다

```java
@Slf4j  
@Component  
@RequiredArgsConstructor  
public class JwtAuthenticationFilter extends OncePerRequestFilter {  
  
   private final JwtTokenProvider jwtTokenProvider;  
   private final TokenRepository tokenRepository;  
   private static String TOKEN_HEADER = "ACCESS-TOKEN";  
  
   @Override  
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,  
      FilterChain filterChain) throws ServletException, IOException {  
      try {  
         String jwt = getJwtFromRequest(request);  
         if (isValidToken(jwt)) {  
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);  
            SecurityContextHolder.getContext().setAuthentication(authentication);  
         }  
      } catch (Exception e) {  
         throw new TokenAuthenticationFilterException(e);  
      }  
      filterChain.doFilter(request, response);  
   }  
  
   private boolean isValidToken(String jwt) {  
      return StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)  
         && !tokenRepository.existsLogoutAccessTokenById(jwt)  
         && !tokenRepository.existsLogoutRefreshTokenById(jwt);  
   }  
  
   private String getJwtFromRequest(HttpServletRequest request) {  
      String bearerToken = request.getHeader(TOKEN_HEADER);  
      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtTokenProvider.TOKEN_TYPE)) {  
         return bearerToken.substring(7);  
      }  
      return null;  
   }  
  
}
```

1. 헤더에서 accesstoken 을 가져옵니다
2. 그리고 accesstoken 저장소(logoutAccessTokenRedisRepository) 에서 유효한 토큰인지 확인합니다
3. 이후, jwtTokenProvider에서 토큰의 유효기간을 체크합니다
4. jwtTokenProvider에서 인증객체(Authentication) 를 가져오고 SecurityContextHolder에 해당 인증 객체를 저장합니다

Authentication

### (2) LoginProcessingAuthenticationFilter
AbstractAuthenticationProcessingFilter 상속받아 커스텀화 하였습니다
LoginProcessingAuthenticationFilter 사용자가 로그인 요청을 할 때, 올라른 로그인 정보를 가졌는지 확인하는 필터입니다

```java

public class LoginProcessingAuthenticationFilter extends AbstractAuthenticationProcessingFilter {  
  
   public LoginProcessingAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {  
      super(requiresAuthenticationRequestMatcher);  
   }  
  
   @Override  
   public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws  
      AuthenticationException, IOException {  
      LoginRequest loginRequest = getLoginForm(request);  
      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(  
         loginRequest.getEmail(), loginRequest.getPassword());  
      return super.getAuthenticationManager().authenticate(authRequest);  
   }  
  
   private LoginRequest getLoginForm(HttpServletRequest request) throws IOException {  
      ServletInputStream inputStream = request.getInputStream();  
      String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);  
      ObjectMapper objectMapper = new ObjectMapper();  
      return objectMapper.readValue(messageBody, LoginRequest.class);  
   }  
  
}

```

1. 먼저 post로 요청이 온것인지 확인합니다 -> 로그인 정보가 requestbody에  들어가 있어야하기 때문 ㅎ
2. HttpServletRequest 의 InputStream 을 통해 requestbody 에 접근합니다
3. objectMapper.readValue 를 통해 LoginForm 객체에 담아서 내용을 가져옵니다
4. 가져온 LoginForm 에서 email, password에 빈칸이 있는지 체크합니다
5. email, password 정보를 UsernamePasswordAuthenticationToken 객체에 담고 
6. 이를 AuthenticationManager 인증 관리자 에게 전달해줍니다
	- 나중에 AuthenticationManager에서 로그인 정보가 맞는지 확인해주는 객체입니다




## 2. handler 패키지

인증 실패, 인증 작업을 관리하는 패키지 입니다

### (1) LoginFailureHandler

AuthenticationFailureHandler 상속받아 커스텀화 하였습니다
인증 실패시, 추후 작업 핸들러입니다

```java
  
@Component  
@RequiredArgsConstructor  
public class LoginFailureHandler implements AuthenticationFailureHandler {  
  
   private final ObjectMapper objectMapper;  
  
   @Override  
   public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,  
      AuthenticationException exception) throws IOException {  
      String errorMessage = "Invalid Username or Password";  
      if (exception instanceof BadCredentialsException) {  
         errorMessage = "Invalid Username or Password";  
      } else if (exception instanceof DisabledException) {  
         errorMessage = "Locked";  
      } else if (exception instanceof CredentialsExpiredException) {  
         errorMessage = "Expired password";  
      }  
      setHttpResponseOption(response, errorMessage);  
   }  
  
   private void setHttpResponseOption(HttpServletResponse response, String errorMessage) throws IOException {  
      response.setStatus(HttpStatus.UNAUTHORIZED.value());  
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);  
      objectMapper.registerModule(new JavaTimeModule());  
      objectMapper.writeValue(response.getWriter(),  
         CommonResponse.failOf(errorMessage, HttpStatus.UNAUTHORIZED.toString()));  
   }  
}

```

1. 401 상태로 인증 실패 상태를 설정합니다
2. json 형태로 반환할것이면 예외 종류에 따라 에러메시지와 함께 감싸서 보냅니다


### (2) LoginSuccessHandler
 
AuthenticationSuccessHandler 상속받아 커스텀화 하였습니다


로그인 정보를 확인하는 핸들러입니다

```java
@Slf4j  
@Component  
@RequiredArgsConstructor  
public class LoginSuccessHandler implements AuthenticationSuccessHandler {  
  
   private final JwtTokenProvider jwtTokenProvider;  
   private final ObjectMapper objectMapper;  
   private final LoginService loginService;  
  
   @Override  
   public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,  
      Authentication authentication) throws IOException {  
      String user = (String)authentication.getPrincipal();  
      LoginResult loginResult = loginService.login(user);  
      setHttpResponseOption(response, loginResult);  
   }  
  
   private void setHttpResponseOption(HttpServletResponse response, LoginResult loginResult) throws IOException {  
      response.setStatus(HttpStatus.OK.value());  
      response.setContentType("application/json");  
      response.setCharacterEncoding("UTF-8");  
      response.setHeader(HttpHeaders.SET_COOKIE,  
         jwtTokenProvider.createCookie(loginResult.getRefreshToken()).toString());  
      objectMapper.registerModule(new JavaTimeModule());  
      objectMapper.writeValue(response.getWriter(), LoginResponse.from(loginResult));  
   }  
  
}
```

1. 인증 성공 상태 코드 200 을 설정합니다
2. 인증 객체(Authentication) 에서 email 정보를 가져와서 프론트한테 전달할 사용자 정보를 json으로 담아서 전달합니다
3. jwtTokenProvider에서 사용자 email 정보로 토큰을 생성하고 json 과 함께 담아서 전달합니다
4. 

### (3) RestAccessDeniedHandler

AccessDeniedHandler 상속받아 커스텀화 하였습니다

사용자 권한이 없는 경우의 핸들러 입니다
```java
@Slf4j  
public class RestAccessDeniedHandler implements AccessDeniedHandler {  
  
   private final String MESSAGE = "권한이 없습니다.";  
  
   @Override  
   public void handle(HttpServletRequest request, HttpServletResponse response,  
      AccessDeniedException accessDeniedException)  
      throws IOException {  
      response.sendError(HttpServletResponse.SC_FORBIDDEN, MESSAGE);  
   }  
}

```

인가 실패 메세지와 인가실패 상태 코드 403를 프론트한테 보냅니다

### (4) RestAuthenticationEntryPoint

AuthenticationEntryPoint 상속받아 커스텀화 하였습니다

인증 실패시 프론트한테 전달할 메세지를 관리하는 핸들러입니다

```java
@Slf4j  
@Component  
@RequiredArgsConstructor  
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {  
  
   private final ObjectMapper objectMapper;  
  
   @Override  
   public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws  
      IOException {  
      SendErrorUtil.sendUnauthorizedErrorResponse(response, objectMapper);  
   }  
  
}
```

인증 실패시 에서 메시지와 실패 메시지 와 함께 보냅니다 

아래는 제가 따로 만든 실패 메시지를 관리하는 클래스(SendErrorUtil) 일부입니다

```java
public static void sendUnauthorizedErrorResponse(HttpServletResponse response, ObjectMapper objectMapper) throws  
   IOException {  
   String errorResponse = objectMapper.writeValueAsString(  
      CommonResponse.failOf(UNAUTHORIZED_MESSAGE, UNAUTHORIZED_CODE));  
   response.setStatus(HttpStatus.UNAUTHORIZED.value());  
   writeErrorResponse(response, errorResponse);  
}
```


## 3. provider 패키지

### (1) CustomAuthenticationProvider

AuthenticationProvider 상속받아 커스텀화 하였습니다

사용자가 입력한 로그인 정보를 검증하는 provider 입니다

```java
@RequiredArgsConstructor  
@Component  
@Slf4j  
public class CustomAuthenticationProvider implements AuthenticationProvider {  
  
   private final UserDetailServiceImpl userDetailServiceImpl;  
   private final PasswordEncoder passwordEncoder;  
  
   @Override  
   public Authentication authenticate(Authentication authentication) throws AuthenticationException {  
      String username = authentication.getName();  
      String password = (String)authentication.getCredentials();  
      UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(username);  
      if (!passwordEncoder.matches(password, userDetails.getPassword())) {  
         throw new BadCredentialsException("BadCredentialsException");//비밀번호가 틀린거  
      }  
      UsernamePasswordAuthenticationToken authenticationToken  
         = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());  
      return authenticationToken;  
   }  
  
   @Override  
   public boolean supports(Class<?> aClass) {  
      return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);  
   }  
}
```

1. 사용자의 비밀번호를 가 올바른지 검증합니다
2. 인증에 성공하였으면, UsernamePasswordAuthenticationToken 객체에 인증정보
	- userDetailServiceImpl에서 꺼내온 new AccountContext(member, grantedAuthorities) 의 grantedAuthorities(인가 정보)  와
	- 사용자 이메일
	- 을 담습니다


### (2) JwtExpirationEnums

 상속받아 커스텀화 하였습니다

토큰 정보관련 상세 정보는 "싱글톤으로 데이터를 관리하는 enum" 으로 관리하였습니다.

```java
public enum JwtExpirationEnums {  
  
   ACCESS_TOKEN_EXPIRATION_TIME("JWT 만료 시간 / 300분", 10000L * 60 * 30),  
   REFRESH_TOKEN_EXPIRATION_TIME("Refresh 토큰 만료 시간 / 7일", 1000L * 60 * 60 * 24 * 7),  
   REISSUE_EXPIRATION_TIME("Refresh 토큰 만료 시간 / 3일", 1000L * 60 * 60 * 24 * 3);  
  
   private String description;  
   private Long value;  
}
```

### (3) JwtTokenProvider 

```java
  
@Slf4j  
@Component  
public class JwtTokenProvider {  
  
   public static final String TOKEN_TYPE = "Bearer ";  
   public static final String REFRESH_TOKEN = "refreshToken";  
   public static final String ACCESS_TOKEN = "ACCESS-TOKEN";  
  
   private final String secretKey;  
   private final UserDetailsService userDetailsService;  
   private final long accessTokenExpirationTimeInMilliSeconds;  
   private final long refreshTokenExpirationTimeInMilliSeconds;  
   private final long reissueRefreshTokenTimeInMilliSeconds;  
  
   public JwtTokenProvider(  
      @Value("${jwt.secret-key}") String secretKey,  
      @Value("${jwt.access-expiration-time}") long accessTokenExpirationTimeInMilliSeconds,  
      @Value("${jwt.refresh-expiration-time}") long refreshTokenExpirationTimeInMilliSeconds,  
      @Value("${jwt.reissue-refresh-time}") long reissueRefreshTokenTimeInMilliSeconds,  
      UserDetailsService userDetailsService  
   ) {  
      this.secretKey = secretKey;  
      this.accessTokenExpirationTimeInMilliSeconds = accessTokenExpirationTimeInMilliSeconds;  
      this.refreshTokenExpirationTimeInMilliSeconds = refreshTokenExpirationTimeInMilliSeconds;  
      this.reissueRefreshTokenTimeInMilliSeconds = reissueRefreshTokenTimeInMilliSeconds;  
      this.userDetailsService = userDetailsService;  
   }  
  
   public String createAccessToken(Authentication authentication) {  
      return createToken(authentication, accessTokenExpirationTimeInMilliSeconds);  
   }  
  
   public String createRefreshToken(Authentication authentication) {  
      return createToken(authentication, refreshTokenExpirationTimeInMilliSeconds);  
   }  
  
   private String createToken(Authentication authentication, long accessTokenExpirationTimeInMilliSeconds) {  
      String userPrincipal = (String)authentication.getPrincipal();  
  
      Date now = new Date();  
      Date expiryDate = new Date(now.getTime() + accessTokenExpirationTimeInMilliSeconds);  
  
      return Jwts.builder()  
         .setSubject(userPrincipal)  
         .setIssuedAt(now)  
         .setExpiration(expiryDate)  
         .signWith(SignatureAlgorithm.HS512, secretKey)  
         .compact();  
   }  
  
   public ResponseCookie createCookie(final String refreshToken) {  
      return createTokenCookieBuilder(refreshToken)  
         .maxAge(Duration.ofMillis(JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME.getValue()))  
         .build();  
   }  
  
   private ResponseCookie.ResponseCookieBuilder createTokenCookieBuilder(final String value) {  
      return ResponseCookie.from(REFRESH_TOKEN, value)  
         .httpOnly(true)  
         .secure(true)  
         .path("/")  
         .sameSite(Cookie.SameSite.NONE.attributeValue());  
   }  
  
   public String removeType(String token) {  
      return token.substring(TOKEN_TYPE.length());  
   }  
  
   public boolean isMoreThanReissueTime(String token) {  
      return getRemainingMilliSecondsFromToken(token) >= reissueRefreshTokenTimeInMilliSeconds;  
   }  
  
   public Authentication getAuthentication(String token) {  
      UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));  
      return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());  
   }  
  
   public String getUserPk(String token) {  
      return Jwts.parser()  
         .setSigningKey(secretKey)  
         .parseClaimsJws(token)  
         .getBody()  
         .getSubject();  
   }  
  
   public boolean validateToken(String authToken) {  
      try {  
         Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);  
         return true;      } catch (JwtException | IllegalArgumentException e) {  
         return false;  
      }  
   }  
  
   private Claims getClaims(String token) {  
      return Jwts.parser()  
         .setSigningKey(secretKey)  
         .parseClaimsJws(token)  
         .getBody();  
   }  
  
   public long getRemainingMilliSecondsFromToken(String token) {  
      Date expiration = getClaims(token).getExpiration();  
      return expiration.getTime() - (new Date()).getTime();  
   }  
  
}
```
- 토큰을 생성 역할을 하는 패키지 입니다 

## 3. service 패키지

### (1) UserDetailServiceImpl

UserDetailsService 상속받아 커스텀화 하였습니다
@Service("userDetailsService") 클래스 상단에 userDetailsService 이름으로 빈 등록해야해야 다른 클래스에서 private final UserDetailService userserivce로 생성자 주입을 해도 알아서 인식합니다.

```java
@Override  
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {  
  
   Member member = memberJpaRepository.findOptionByEmail(email)  
      .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));  
  
   List<GrantedAuthority> grantedAuthorities = Collections  
      .singletonList(new SimpleGrantedAuthority(member.getRole()));  
  
   return new AccountContext(member, grantedAuthorities);  
}
```

- loadUserByUsername 메서드에서 실제 db의 회원정보를 가져옵니다
- 해당 메서드는 AccountContext 객체에 사용자 정보와 인가 정보를 담아서 UserDetail 로 반환합니다  
- 원래는 User 라는 객체로 반환해야 하지만 아래와 같이  User 를 상속받아 AccountContext 클래스를 만들어 커스텀화 하였습니다.


```java
@Getter  
public class AccountContext extends User {  
  
   private Member member;  
  
   public AccountContext(Member member, Collection<? extends GrantedAuthority> authorities) {  
      super(member.getEmail(), member.getPassword(), authorities);  
      this.member = member;  
   }  
}

```


> 커스텀화 한 이유: 이후 매번 로그인 할때마다 db 에서 회원정보를 조회하는 번거로움을 해결하기 위해 member 라는 객체를 담을수 있게 하였습니다. 

원래는 User 객체에 아래와 같은 정보밖에 담지 못합니다.

```java
public User(String username, String password, boolean enabled, boolean accountNonExpired,  
      boolean credentialsNonExpired, boolean accountNonLocked,  
      Collection<? extends GrantedAuthority> authorities) {  
   Assert.isTrue(username != null && !"".equals(username) && password != null,  
         "Cannot pass null or empty values to constructor");  
   this.username = username;  
   this.password = password;  
   this.enabled = enabled;  
   this.accountNonExpired = accountNonExpired;  
   this.credentialsNonExpired = credentialsNonExpired;  
   this.accountNonLocked = accountNonLocked;  
   this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));  
}

//또는

public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {  
   this(username, password, true, true, true, true, authorities);  
}
```

> AccountContext 객체를 사용하여 member 객체를 담고 
> @LoginUser 라는 어노테이션을 만들어 argumentResolver를 통해 바로 사용자의 정보를 받아올수 있게 하였습니다.

```java
/**  
 * 만약, domain 으로 생성된 객체 클래스명이 Member 아니라 예를 들어 LoginUser 라면 SpEL에 member 가 아니라 loginUser 를 반환하도록 해야 합니다.  
 */
@Retention(RetentionPolicy.RUNTIME) // runtime시 까지 유지  
@Target(ElementType.PARAMETER) // 파라미터에 사용할 것  
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : member")  
public @interface LoginUser {  
}
```

- 아래는 실제 사용한 예입니다, 이렇게 controller에서  @LoginUser Member member 를 불러올수 있게 하였습니다.

```java
@GetMapping("/user/spot/{spotId}")  
public SpotDetailResponse spotDetail(@PathVariable("spotId") Long spotId, @LoginUser Member member) {  
   SpotDetailDto spotDetailDto = detailSpotFacade.spotPage(spotId, member.getId());  
   return new SpotDetailResponse(200l, true, "성공", spotDetailDto);  
  
}
```

탈퇴한 회원의 토큰 blacklist 로 관리  
```java
LogoutAccessToken logoutAccessToken = LogoutAccessToken  
   .createLogoutAccessToken(accessToken, member.getEmail(),  
      JwtExpirationEnums.ACCESS_TOKEN_EXPIRATION_TIME.getValue());

```
