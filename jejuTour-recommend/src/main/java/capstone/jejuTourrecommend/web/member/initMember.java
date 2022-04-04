package capstone.jejuTourrecommend.web.member;


import capstone.jejuTourrecommend.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile("local")
@Component
@RequiredArgsConstructor
public class initMember {

    private final InitMemberService initMemberService;

    @PostConstruct
    public void init(){
        initMemberService.init();
    }

    @Component
    static class InitMemberService{
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init(){
            Member memberA = new Member("memberA","123","123");
            Member memberB = new Member("memberB","234","234");

            em.persist(memberA);
            em.persist(memberB);
        }
    }

}

















