package capstone.jejuTourrecommend.config.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {//jwt토큰 제공자

	private final UserDetailsService userDetailsService;
	@Value("${jwt.secret}")
	private String secretKey;
	private String refreshKey = "webfirewood1";

	// 객체 초기화, secretKey를 Base64로 인코딩한다.
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		refreshKey = Base64.getEncoder().encodeToString(refreshKey.getBytes());
	}

	/**
	 * 나여기서 권한 한개만 갖는걸로 함 list<String> roles 을 string role으로 바꿈
	 */
	// JWT 토큰 생성
	//여기서 내가 넣었준 값은 회원 이메일과 역할이다
	public String createToken(String userPk, String role) {
		Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
		//setSubject 는 unique 값으로 설정한다. 내가 설정한 email 은 unique 함

		claims.put("roles", role); // 정보는 key / value 쌍으로 저장된다.

		Date now = new Date();
		return Jwts.builder()
			.setClaims(claims) // 정보 저장
			.setIssuedAt(now) // 토큰 발행 시간 정보
			.setExpiration(
				new Date(now.getTime() + JwtExpirationEnums.ACCESS_TOKEN_EXPIRATION_TIME.getValue())) // set Expire Time
			.signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
			// signature 에 들어갈 secret 값 세팅
			.compact();
	}

	public String createRefreshToken(String userPk, String role) {

		Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
		//setSubject 는 unique 값으로 설정한다. 내가 설정한 email 은 unique 함

		claims.put("roles", role); // 정보는 key / value 쌍으로 저장된다.

		Date now = new Date();

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME.getValue()))
			.signWith(SignatureAlgorithm.HS256, refreshKey)
			.compact();
	}

	// JWT 토큰에서 인증 정보 조회
	// loadUserByUsername 으로 회원정보를 가져옴 (참고로 나는 pk를 username 이 아니라 email 로 함)
	public Authentication getAuthentication(String token) {
		//Todo: 나 여기서 나만의 userDetailservice 만들어서 jpa 하고 연결시겨야함
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));

		//여기서 userDetail을 넣어야지 userDetails.getUsername() 를 넣으면 안됨!! @AuthenticationPrincipal 가 인식을 못함,
		//파라미터 자리가 principal 이라고 해서 진짜 principal 넣으면 안됨
		//UsernamePasswordAuthenticationToken 는 AuthenticationToken 의 인증 객체임
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	//access 토큰에서 회원 정보(이메일) 추출******
	public String getUserPk(String token) {
		return Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	//refresh 토크에서 "member 이메일" 가져오기
	public String getUserPkByRefreshToken(String token) {
		return Jwts.parser()
			.setSigningKey(refreshKey)
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	// Request의 Header에서 token 값을 가져옵니다. "ACCESS-TOKEN" : "TOKEN값'
	public String resolveToken(HttpServletRequest request) {
		return request
			.getHeader("ACCESS-TOKEN");
	}//이거 없어도 됨 @RequestHeader어노테이션이 있음//아님 있어야 함 filter에서 씀

	public String resolveRefreshToken(HttpServletRequest request) {
		return request.getHeader("REFRESH-TOKEN");//REFRESH-TOKEN
	}

	// 토큰의 유효성 + 만료일자 확인
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(jwtToken);

			return !claims.getBody()
				.getExpiration()
				.before(new Date());// 만료날짜가 현재날짜(Date)이전이지 않으면 반환

		} catch (Exception e) {         // 이후면 false 반환
			log.info("만료된 토큰입니다 from validateToken");

			return false;
		}
	}

	public boolean isValidRefreshToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser()
				.setSigningKey(refreshKey)
				.parseClaimsJws(token);

			return !claims.getBody()
				.getExpiration()
				.before(new Date());// 만료날짜가 현재날짜(Date)이전이지 않으면 반환

		} catch (Exception e) {         // 이후면 false 반환
			log.info("만료된 토큰입니다");
			//throw new UserException("만료된 혹은 잘못된 토큰입니다");
			return false;
		}
	}

}






