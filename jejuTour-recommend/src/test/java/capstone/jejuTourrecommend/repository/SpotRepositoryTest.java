package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.Location;
import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.domain.Score;
import capstone.jejuTourrecommend.domain.Spot;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@Commit
class SpotRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired SpotRepository spotRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void before(){
        Random random = new Random();

        Score[] scores = new Score[100];
        Spot[] spots = new Spot[100];

        for(int i=0;i<100;i++){ //지역하고 score만

            if(0<=i&&i<25) {
                spots[i] = new Spot(Location.Aewol_eup,createScore(scores,i));
            }if(25<=i&&i<50){
                spots[i] = new Spot(Location.Aewol_eup,createScore(scores,i));
            }if(50<=i&&i<75){
                spots[i] = new Spot(Location.Andeok_myeon,createScore(scores,i));
            }if(75<=i&&i<100){
                spots[i] = new Spot(Location.Andeok_myeon,createScore(scores,i));
            }

            //spots[i].setScore(scores[i]);
            log.info("spots[i].getScore().toString() = {}",spots[i].getScore().toString());
            em.persist(spots[i]);
        }

        em.flush();
        em.clear();

//        for (Spot spot : spots) {
//            System.out.println("spot = " + spot);
//        }
    }

    public Score createScore(Score[] scores,int i){
        Random random = new Random();
        scores[i]= new Score(
                random.nextDouble()*10,random.nextDouble()*10,
                random.nextDouble()*10,random.nextDouble()*10,
                random.nextDouble()*10,random.nextDouble()*10,
                random.nextDouble()*10,random.nextDouble()*10,
                random.nextDouble()*10);
        return scores[i];
    }



    @Test
    public void SpotLocationCategoryTest() throws Exception{


        //given


        //when

//        //지금 상태가 두 테이블 페치 조인하고 정렬은 합쳐진 것의 property로 하려는데 안되는 거임
//        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "viewScore"));
//        Page<Spot> page = spotRepository.findSpotByLocation(Location.Aewol_eup, pageRequest);




        //then
        List<Spot> content = page.getContent();
        for (Spot spot : content) {
            System.out.println("spot = " + spot);
        }


    }

}