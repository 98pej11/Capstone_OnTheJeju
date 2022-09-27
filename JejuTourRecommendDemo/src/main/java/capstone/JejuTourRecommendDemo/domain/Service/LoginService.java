package capstone.JejuTourRecommendDemo.domain.Service;

import capstone.JejuTourRecommendDemo.domain.Member;
import capstone.JejuTourRecommendDemo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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























