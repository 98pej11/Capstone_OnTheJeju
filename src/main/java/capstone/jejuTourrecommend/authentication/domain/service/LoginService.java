package capstone.jejuTourrecommend.authentication.domain.service;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.authentication.infrastructure.respository.MemberJpaRepository;
import capstone.jejuTourrecommend.authentication.presentation.dto.request.JoinForm;
import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.config.redis.LogoutAccessToken;
import capstone.jejuTourrecommend.config.redis.LogoutAccessTokenRedisRepository;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

	private final MemberJpaRepository memberJpaRepository;
	private final PasswordEncoder passwordEncoder;
	private final MemberSpotJpaRepository memberSpotJpaRepository;
	private final SpotJpaRepository spotJpaRepository;
	private final EntityManager em;
	private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

	/**
	 * 회원 가입
	 */
	public UserDto join(JoinForm form) {
		validateDuplicateMember(form.getEmail());
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
		memberSpotJpaRepository.bulkDeleteMemberSpotByMember(member);
		memberJpaRepository.deleteById(member.getId());
		//여기서 selectId 만하고 delete 는 이 메서드 전체 끝날때 처리함 그래서 em.flush, clear 로 바로 반영하게 함
		em.flush();
		em.clear();
		//탈퇴한 회원의 토큰 blacklist 로 관리
		LogoutAccessToken logoutAccessToken = LogoutAccessToken
			.createLogoutAccessToken(accessToken, member.getEmail(),
				JwtExpirationEnums.ACCESS_TOKEN_EXPIRATION_TIME.getValue());
		logoutAccessTokenRedisRepository.save(logoutAccessToken);
	}

	public void makeMemberSpot(Member member) {
		List<Spot> spotList = spotJpaRepository.findAll();
		int size = spotList.size();
		List<MemberSpot> memberSpots = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			memberSpots.add(new MemberSpot(0d, member, spotList.get(i)));
		}
		//Todo: save를 스프링 데이터 jpa가 제공해주는 saveAll로 개선
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

}























