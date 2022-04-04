package capstone.jejuTourrecommend.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Commit
class ScoreTest {


    @PersistenceContext
    EntityManager em;


    @Test
    public void score_spotTest() throws Exception{

        Score score1 = new Score();
        Score score2 = new Score();

        em.persist(score1);
        em.persist(score2);

        //spot이 주인임
        Spot spot1 = new Spot("spot1",score1);
        Spot spot2 = new Spot("spot2",score2);

        em.persist(spot1);
        em.persist(spot2);

        em.flush();
        em.clear();

        List<Spot> resultList = em.createQuery("select s from Spot s", Spot.class)
                .getResultList();

        for (Spot spot : resultList) {
            System.out.println("spot = " + spot);
            System.out.println("spot.getScore() = " + spot.getScore());
        }

    }

}