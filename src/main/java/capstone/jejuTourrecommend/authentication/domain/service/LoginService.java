package capstone.jejuTourrecommend.authentication.domain.service;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.authentication.infrastructure.respository.MemberJpaRepository;
import capstone.jejuTourrecommend.authentication.presentation.dto.request.JoinForm;
import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.config.redis.LogoutAccessToken;
import capstone.jejuTourrecommend.config.redis.LogoutAccessTokenRedisRepository;
import capstone.jejuTourrecommend.config.security.dto.TokenResponse;
import capstone.jejuTourrecommend.config.security.dto.UserDto;
import capstone.jejuTourrecommend.config.security.provider.JwtExpirationEnums;
import capstone.jejuTourrecommend.config.security.provider.JwtTokenProvider;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.mainSpot.MemberSpot;
import capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot.MemberSpotJpaRepository;
import capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot.SpotJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

	private final MemberJpaRepository memberJpaRepository;

	private final PasswordEncoder passwordEncoder;

	private final JwtTokenProvider jwtTokenProvider;

	//Todo: 업데이트
	private final MemberSpotJpaRepository memberSpotJpaRepository;
	private final SpotJpaRepository spotJpaRepository;

	private final EntityManager em;
	private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

	/**
	 * 회원 가입
	 */
	public UserDto join(JoinForm form) {

		//유저 중복성 검사(예외처리)
		validateDuplicateMember(form.getEmail());

		//비밀번호 암호화 하여 디비에 저장하기위한 작업//테스트를 위해 일단 사용X
		String encodedPassword = passwordEncoder.encode(form.getPassword());

		Member member = new Member(
			form.getUsername(), form.getEmail(), encodedPassword, "ROLE_USER"   //"USER" 아님
		);

		//유저 저장
		memberJpaRepository.save(member);

		UserDto userDto = memberJpaRepository.findOptionByEmail(form.getEmail())
			.map(m -> new UserDto(m.getId(), m.getUsername(),
				m.getEmail(), m.getRole(), m.getCreatedDate(),
				m.getLastModifiedDate()))
			.orElseThrow(() -> new UserException("db에 회원 저장인 안됐습니다. "));

		//String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

		//System.out.println(userDto.getLastModifiedDate());

		makeMemberSpot(member);

		return userDto;
	}

	/**
	 * logout 구현
	 */
	public void logout(String accessToken, String userEmail) {

		LogoutAccessToken logoutAccessToken = LogoutAccessToken
			.createLogoutAccessToken(accessToken, userEmail,
				JwtExpirationEnums.ACCESS_TOKEN_EXPIRATION_TIME.getValue());

		logoutAccessTokenRedisRepository.save(logoutAccessToken);

	}

	/**
	 * 회원 탈퇴
	 */
	public void deleteMember(Member member, String accessToken) {

		System.out.println("=======================1");
		memberSpotJpaRepository.bulkDeleteMemberSpotByMember(member);

		System.out.println("=======================2");
		memberJpaRepository.deleteById(member.getId());
		//여기서 selectId 만하고 delete 는 이 메서드 전체 끝날때 처리함 그래서 em.flush, clear 로 바로 반영하게 함
		em.flush();
		em.clear();
		System.out.println("=======================3");

		//탈퇴한 회원의 토큰 blacklist 로 관리
		LogoutAccessToken logoutAccessToken = LogoutAccessToken
			.createLogoutAccessToken(accessToken, member.getEmail(),
				JwtExpirationEnums.ACCESS_TOKEN_EXPIRATION_TIME.getValue());

		logoutAccessTokenRedisRepository.save(logoutAccessToken);
		System.out.println("=======================4");

	}

	public void makeMemberSpot(Member member) {

		List<Spot> spotList = spotJpaRepository.findAll();
		int size = spotList.size();

		//MemberSpot[] memberSpots = new MemberSpot[size];
		List<MemberSpot> memberSpots = new ArrayList<>();

		for (int i = 0; i < size; i++) {
			memberSpots.add(new MemberSpot(0d, member, spotList.get(i)));

			log.info("memberSpots.toArray() = {}", memberSpots.toArray());
		}
		//Todo: save를 스프링 데이터 jpa가 제공해주는 saveAll로 고침
		memberSpotJpaRepository.saveAllAndFlush(memberSpots);

		em.clear();

	}

	//회원 중복 검사
	private void validateDuplicateMember(String email) {

		//이메일로 회원 조회
		memberJpaRepository.findOptionByEmail(email)
			.ifPresent(member -> {
				throw new UserException("이미 존재하는 E-MAIL입니다");
			});

	}

	//이부분 없어도 됨
	public TokenResponse issueAccessToken(HttpServletRequest request) {
		String accessToken = jwtTokenProvider.resolveToken(request);

		String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

		log.info("accessToken = {}", accessToken);
		log.info("refreshToken = {}", refreshToken);

		//        Member member1 = memberRepository.findOptionByRefreshToken(refreshToken)
		//                .orElseThrow(() -> new UserException("db와 일치하지 않음"));
		//        log.info("db와 일치");
		//log.info("member1.getRefreshToken() = {}",member1.getRefreshToken());

		//accessToken이 만료됐고 refreshToken이 맞으면 accessToken을 새로 발급(refreshToken의 내용을 통해서)
		if (!jwtTokenProvider.validateToken(accessToken)) {  //클라이언트에서 토큰 재발급 api로의 요청을 확정해주면 이 조건문은 필요없다.
			log.info("Access 토큰 만료됨");

			if (jwtTokenProvider.isValidRefreshToken(refreshToken)) {     //들어온 Refresh 토큰이 유효한지
				log.info("Refresh 토큰은 유효함");

				//받은 refresh토큰을 가지고 db에서 찾아보기

				String userPkByRefreshToken = jwtTokenProvider.getUserPkByRefreshToken(refreshToken);

				Member member = memberJpaRepository.findOptionByEmail(userPkByRefreshToken)
					.orElseThrow(() -> new UserException("일치하지 않은 refresh 토큰입니다"));

				if (refreshToken.equals(member.getRefreshToken())) {   //DB의 refresh 토큰과 지금들어온 토큰이 같은지 확인
					log.info("accesstoken 을 다시 만들어주었습니다");
					accessToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole());
				} else {
					//DB의 Refresh토큰과 들어온 Refresh토큰이 다르면 중간에 변조된 것임
					throw new UserException("변조된 refresh 토큰이 들어왔습니다");
					//예외발생
				}
			} else {//Refresh 토큰이 제대로 안들어올 경우
				throw new UserException("들어온 Refresh 토큰은 유효하지 않습니다. ");
			}
		}
		return TokenResponse.builder()
			.ACCESS_TOKEN(accessToken)
			.REFRESH_TOKEN(refreshToken)
			.build();
	}

}























