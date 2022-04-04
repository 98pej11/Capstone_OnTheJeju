package capstone.jejuTourrecommend.domain.Service;

import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String email, String password){

        return  memberRepository.findOptionByEmail(email)
                .filter(m->m.getPassword().equals(password))
                .orElse(null);
    }




}























