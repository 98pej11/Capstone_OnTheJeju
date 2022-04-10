package capstone.jejuTourrecommend.web.login;


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

    private final InitUserService initUserService;

    @PostConstruct
    public void init(){
        initUserService.init();
    }

    @Component
    static class InitUserService{
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init(){
            Member memberA = new Member("userA","123","123");
            Member memberB = new Member("userB","234","234");

            em.persist(memberA);
            em.persist(memberB);
        }
    }

}

















