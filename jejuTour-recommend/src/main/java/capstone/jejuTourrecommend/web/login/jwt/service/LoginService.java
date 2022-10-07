package capstone.jejuTourrecommend.web.login.jwt.service;

import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.domain.MemberSpot;
import capstone.jejuTourrecommend.domain.Spot;
import capstone.jejuTourrecommend.repository.MemberRepository;
import capstone.jejuTourrecommend.repository.MemberSpotRepository;
import capstone.jejuTourrecommend.repository.SpotRepository;
import capstone.jejuTourrecommend.web.login.dto.UserDto;
import capstone.jejuTourrecommend.web.login.exceptionClass.UserException;
import capstone.jejuTourrecommend.web.login.dto.form.JoinForm;
import capstone.jejuTourrecommend.web.login.jwt.provider.JwtExpirationEnums;
import capstone.jejuTourrecommend.web.login.jwt.provider.JwtTokenProvider;
import capstone.jejuTourrecommend.web.login.dto.TokenResponse;
import capstone.jejuTourrecommend.web.redis.LogoutAccessToken;
import capstone.jejuTourrecommend.web.redis.LogoutAccessTokenRedisRepository;
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

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;


    //Todo: 업데이트
    private final MemberSpotRepository memberSpotRepository;
    private final SpotRepository spotRepository;

    private final EntityManager em;


    /**
     * 회원 가입
     */
    public UserDto join(JoinForm form) {

        //유저 중복성 검사(예외처리)
        validateDuplicateMember(form.getEmail());

        //비밀번호 암호화 하여 디비에 저장하기위한 작업//테스트를 위해 일단 사용X
        String encodedPassword = passwordEncoder.encode(form.getPassword());


        //이게 refresh토큰을 만들고 db에 저장해주기(회원가입때는 accesstoken생성하지 않음, 로그인할때 사용합)
        //String refreshToken = jwtTokenProvider.createRefreshToken(form.getEmail(),"ROLE_USER");

        Member member = new Member(
                form.getUsername(), form.getEmail(), encodedPassword, "ROLE_USER"   //"USER" 아님
        );
        //유저 저장
        memberRepository.save(member);


        UserDto userDto = memberRepository.findOptionByEmail(form.getEmail())
                .map(m -> new UserDto(m.getId(), m.getUsername(),
                        m.getEmail(), m.getRole(), m.getCreatedDate(),
                        m.getLastModifiedDate()))
                .orElseThrow(() -> new UserException("db에 회원 저장인 안됐습니다. "));

        //String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        //System.out.println(userDto.getLastModifiedDate());

        makeMemberSpot(member);


        return userDto;
    }

    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    /**
     * logout 구현
     */
    public void logout(String accessToken, String userEmail) {

        LogoutAccessToken logoutAccessToken = LogoutAccessToken
                .createLogoutAccessToken(accessToken, userEmail, JwtExpirationEnums.ACCESS_TOKEN_EXPIRATION_TIME.getValue());

        logoutAccessTokenRedisRepository.save(logoutAccessToken);

    }


    /**
     * 회원 탈퇴
     */
    public void deleteMember(Member member, String accessToken) {

        System.out.println("=======================1");
        memberSpotRepository.bulkDeleteMemberSpotByMember(member);

        System.out.println("=======================2");
        memberRepository.deleteById(member.getId());//여기서 selectId 만하고 delete 는 이 메서드 전체 끝날때 처리함
        System.out.println("=======================3");

        //탈퇴한 회원의 토큰 blacklist 로 관리
        LogoutAccessToken logoutAccessToken = LogoutAccessToken
                .createLogoutAccessToken(accessToken, member.getEmail(), JwtExpirationEnums.ACCESS_TOKEN_EXPIRATION_TIME.getValue());

        logoutAccessTokenRedisRepository.save(logoutAccessToken);
        System.out.println("=======================4");

    }

    public void makeMemberSpot(Member member) {

        List<Spot> spotList = spotRepository.findAll();
        int size = spotList.size();


        //MemberSpot[] memberSpots = new MemberSpot[size];
        List<MemberSpot> memberSpots = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            memberSpots.add(new MemberSpot(0d, member, spotList.get(i)));
            //Todo: save를 스프링 데이터 jpa가 제공해주는 saveAll로 고침
            //memberSpotRepository.save(memberSpots[i]);
            log.info("memberSpots.toArray() = {}", memberSpots.toArray());
        }

        memberSpotRepository.saveAllAndFlush(memberSpots);

        //em.flush();
        em.clear();

    }


    //로그인 정보가 일치하는지 확인
    public UserDto login(String email, String password) {
        log.info("email={}, password={}", email, password);

        Member member = memberRepository.findOptionByEmail(email)
                .orElseThrow(() -> new UserException("가입되지 않은 E-MAIL 입니다."));

        /*//테스트를 위해 비밀번호 인코딩 안해놓음
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new UserException("잘못된 비밀번호입니다.");
        }*/

        log.info("password = {}", password);
        log.info("member.getPassword() = {}", member.getPassword());
        if (!member.getPassword().equals(password)) {
            throw new UserException("잘못된 비밀번호입니다.");
        }


        UserDto userDto = new UserDto(
                member.getId(), member.getUsername(), member.getEmail(), member.getRole()
                , member.getCreatedDate(), member.getLastModifiedDate());


        return userDto;
    }

    //회원 중복 검사
    private void validateDuplicateMember(String email) {

        //이메일로 회원 조회
        memberRepository.findOptionByEmail(email)
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

                Member member = memberRepository.findOptionByEmail(userPkByRefreshToken)
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























