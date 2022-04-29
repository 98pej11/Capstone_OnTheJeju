package capstone.jejuTourrecommend.domain.Service;

import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.repository.MemberRepository;
import capstone.jejuTourrecommend.web.login.UserDto;
import capstone.jejuTourrecommend.web.login.exceptionClass.UserException;
import capstone.jejuTourrecommend.web.login.form.JoinForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입
     */
    public UserDto join(JoinForm form){

        memberRepository.findOptionByEmail(form.getEmail())
                .ifPresent(member-> {
                    throw new UserException("이미 존재하는 E-MAIL입니다");
                });



        //비밀번호 암호화 하여 디비에 저장하기
        String encodedPassword = passwordEncoder.encode(form.getPassword());

        Member member = new Member(
                form.getUsername(), form.getEmail(), encodedPassword,"ROLE_USER"
        );

        validateDuplicateMember(member);
        memberRepository.save(member);

        

        UserDto userDto = memberRepository.findOptionByEmail(form.getEmail())
                .map(member1 -> new UserDto(member1.getId(), member1.getUsername(),
                        member1.getEmail(),member1.getRole()))
                .orElseThrow(() -> new UserException("db에 회원 저장인 안됐습니다. "));



        return userDto;
    }

    //로그인 정보가 일치하는지 확인
    public UserDto login(String email, String password){
        log.info("email={}, password={}",email,password);

        Member member = memberRepository.findOptionByEmail(email)
                .orElseThrow(() -> new UserException("가입되지 않은 E-MAIL 입니다."));


        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new UserException("잘못된 비밀번호입니다.");
        }

        UserDto userDto = new UserDto(
                member.getId(),member.getUsername(),member.getEmail(),member.getRole());


        return userDto;
    }

    //회원 중복 검사
    private void validateDuplicateMember(Member member) {

        //이메일로 회원 조회
        List<Member> findMembers = memberRepository.findByEmail(member.getEmail());
        if(!findMembers.isEmpty()){ //비어있지 않으면= 이미 존재하면
            //throw new IllegalStateException("이미 존재하는 회원입니다.");
            throw new UserException("이미 존재하는 회원입니다.");
        }
    }



}























