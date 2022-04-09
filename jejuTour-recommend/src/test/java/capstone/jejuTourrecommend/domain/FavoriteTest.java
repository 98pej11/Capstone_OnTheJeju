package capstone.jejuTourrecommend.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;


@SpringBootTest
@Transactional
//@Rollback(value = false)
class FavoriteTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void favorite_memberTest() throws Exception{
        //given
        User userA = new User("123@gmail.com", "123");
        User userB = new User("234@naver.com", "123");

        em.persist(userA);
        em.persist(userB);

        Favorite favorite1 = new Favorite("favorite1",userA);
        Favorite favorite2 = new Favorite("favorite2",userA);
        Favorite favorite3 = new Favorite("favorite3",userB);
        Favorite favorite4 = new Favorite("favorite4",userB);

        em.persist(favorite1);
        em.persist(favorite2);
        em.persist(favorite3);
        em.persist(favorite4);

        em.flush();
        em.clear();

        List<Favorite> favorites = em.createQuery("select f from Favorite f",Favorite.class)
                .getResultList();

        for (Favorite favorite : favorites) {
            System.out.println("favorite.getName() = " + favorite.getName());
            System.out.println("favorite.getUser() = " + favorite.getUser());
        }

    }

}





























