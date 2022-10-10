package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.Review;
import capstone.jejuTourrecommend.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ReviewDto;
import capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot.ReviewJpaQuerydslRepository;
import capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot.SpotJpaQuerydslRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;


@Slf4j
@SpringBootTest
@Transactional
class ReviewJpaRepositoryImplTest {

    @Autowired
    ReviewJpaQuerydslRepository reviewJpaRepository;

    @Autowired
    SpotJpaQuerydslRepository spotJpaRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void searchReview() throws Exception{
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

        em.flush();
        em.clear();

        Optional<Spot> spot = spotJpaRepository.findOptionById(spot1.getId());

        PageRequest pageRequest = PageRequest.of(0,100);

        //when
        Page<ReviewDto> reviewDtos = reviewJpaRepository.searchSpotReview(spot1, pageRequest);

        for (ReviewDto reviewDto : reviewDtos) {
            System.out.println("reviewDto.getContents() = " + reviewDto.getContent());
        }

        //then
    }







}










