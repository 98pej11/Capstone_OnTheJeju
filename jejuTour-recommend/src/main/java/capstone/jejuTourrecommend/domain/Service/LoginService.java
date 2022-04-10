package capstone.jejuTourrecommend.domain.Service;

import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.repository.MemberRepository;
import capstone.jejuTourrecommend.web.login.form.JoinForm;
import capstone.jejuTourrecommend.web.login.form.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;



    /**
     * 회원 가입
     */
    @Transactional
    public Long join(JoinForm form){
        Member member = new Member(
                form.getUsername(), form.getEmail(), form.getPassword(),"ROLE_USER"
        );

        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByEmail(member.getEmail());
        if(!findMembers.isEmpty()){ //비어있지 않으면= 이미 존재하면
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public Member login(String email, String password){
        log.info("email={}, password={}",email,password);

        return  memberRepository.findOptionByEmail(email)
                .filter(m->m.getPassword().equals(password))
                .orElse(null);
    }

}























