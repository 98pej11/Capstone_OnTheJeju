package capstone.jejuTourrecommend.web;


import capstone.jejuTourrecommend.domain.User;
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
            User memberA = new User("userA","123","123");
            User memberB = new User("userB","234","234");

            em.persist(memberA);
            em.persist(memberB);
        }
    }

}

















