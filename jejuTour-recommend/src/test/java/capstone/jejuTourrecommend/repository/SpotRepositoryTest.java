package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.*;
import capstone.jejuTourrecommend.web.mainPage.SpotLocationDto;
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


    @Autowired SpotRepository spotRepository;

    @PersistenceContext
    EntityManager em;

    //@BeforeEach
    public void before(){
        Random random = new Random();

        Score[] scores = new Score[100];
        Spot[] spots = new Spot[100];

        Picture[][] pictures = new Picture[100][3];

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

            for(int j=0; j<3;j++) {
                pictures[i][j] = new Picture("asdf1",spots[i]);
                em.persist(pictures[i][j]);
            }


            //spots[i].setScore(scores[i]);
            //log.info("spots[i].getScore().toString() = {}",spots[i].getScore().toString());
            //log.info("spots[i].getPictures().toArray() = {}",spots[i].getPictures().toArray());
            //em.persist(spots[i]);
        }


        em.flush();
        em.clear();

        for (Spot spot : spots) {
            System.out.println("spot = " + spot);
        }
    }

    @BeforeEach
    public void before1(){
        Random random = new Random();

        Score[] scores = new Score[5];
        Spot[] spots = new Spot[5];

        Picture[][] pictures = new Picture[5][2];

        for(int i=0;i<5;i++){ //지역하고 score만

            spots[i] = new Spot(Location.Andeok_myeon,createScore(scores,i));

            for(int j=0; j<2;j++) {
                pictures[i][j] = new Picture("asdf1",spots[i]);
                em.persist(pictures[i][j]);
            }

            em.persist(spots[i]);
        }
        em.flush();
        em.clear();

//        for (Spot spot : spots) {
//            System.out.println("spot = " + spot);
//        }
        for (Picture[] picture : pictures) {
            for (Picture picture1 : picture) {
                System.out.println("picture1 = " + picture1);
            }
        }
    }

    public Score createScore(Score[] scores,int i){
        Random random = new Random();
        scores[i]= new Score(
                random.nextDouble()*10,random.nextDouble()*10,
                random.nextDouble()*10,random.nextDouble()*10,
                random.nextDouble()*10,random.nextDouble()*10,
                random.nextDouble()*10,random.nextDouble()*10,
                random.nextDouble()*10);
        em.persist(scores[i]);//여기서 이거 한해도 되는 이유는
        // cascade = CascadeType.ALL를 해놓아서 그런거임, 원래는 넣어줘야 함
        return scores[i];
    }




    @Test
    public void SpotLocationCategoryTest() throws Exception{

        List<SpotLocationDto> spotByLocation = spotRepository.findSpotByLocation(Location.Andeok_myeon);

        for (SpotLocationDto spotLocationDto : spotByLocation) {
            System.out.println("spotLocationDto = " + spotLocationDto);
        }


    }

}










