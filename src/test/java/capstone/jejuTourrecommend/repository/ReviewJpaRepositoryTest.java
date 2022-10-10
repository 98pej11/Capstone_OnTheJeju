package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Review;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot.ReviewJpaRepository;
import capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot.SpotJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ReviewJpaRepositoryTest {

    @Autowired
    ReviewJpaRepository reviewJpaRepository;

    @Autowired
    SpotJpaRepository spotJpaRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void findReview() throws Exception{
        //given
        Spot spot1 = new Spot("spot1");
        em.persist(spot1);

        Review review1 = new Review("fun1",spot1);
        Review review2 = new Review("fun2",spot1);
        Review review3 = new Review("fun3",spot1);
        Review review4 = new Review("fun4",spot1);
        em.persist(review1);
        em.persist(review2);
        em.persist(review3);
        em.persist(review4);

        Picture picture = new Picture("sss",spot1);
        em.persist(picture);

        em.flush();
        em.clear();

        log.info("spot1 = {}",spot1);


        List<Review> result2 = reviewJpaRepository.findBySpot(spot1);
        //then
        assertThat(result2.size()).isEqualTo(4);

    }

}