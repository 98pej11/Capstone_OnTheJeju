package capstone.JejuTourRecommend.domain.Service;

import capstone.JejuTourRecommend.domain.Member;
import capstone.JejuTourRecommend.repository.MemberRepository;
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























