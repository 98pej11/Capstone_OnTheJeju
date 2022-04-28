package capstone.jejuTourrecommend.web;


import capstone.jejuTourrecommend.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Random;

@Slf4j
@Profile("local")
@Component
@RequiredArgsConstructor
public class initData {

    private final InitUserService initUserService;
    private final InitSpotService initSpotService;

    @PostConstruct
    public void init(){
        //initUserService.init();
        initSpotService.init();
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

            em.flush();
            em.clear();
        }
    }

    @Component
    static class InitSpotService{
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init(){

            Member member1 = new Member("member1","member1@gmail.com","1234");
            Member member2 = new Member("member2","member2@naver.com","2345");
            em.persist(member1);
            em.persist(member2);
            log.info("member1 ={}",member1);

            MemberSpot[] memberSpots = new MemberSpot[100];
            Score[] scores = new Score[100];
            Spot[] spots = new Spot[100];
            Picture[][] pictures = new Picture[100][3];
            Review[] reviews = new Review[100];

            for(int i=0;i<100;i++){ //지역하고 score만

                if(0<=i&&i<25) {
                    spots[i] = new Spot(Location.Aewol_eup,createScore(scores,i));
                    memberSpots[i] = new MemberSpot(0d,member2,spots[i]);
                }if(25<=i&&i<50){
                    spots[i] = new Spot(Location.Aewol_eup,createScore(scores,i));
                    memberSpots[i] = new MemberSpot(0d,member2,spots[i]);
                }if(50<=i&&i<75){
                    spots[i] = new Spot(Location.Andeok_myeon,createScore(scores,i));
                    memberSpots[i] = new MemberSpot(0d,member1,spots[i]);
                }if(75<=i&&i<100){
                    spots[i] = new Spot(Location.Andeok_myeon,createScore(scores,i));
                    memberSpots[i] = new MemberSpot(0d,member1,spots[i]);
                }
                for(int j=0; j<3;j++) {
                    pictures[i][j] = new Picture("asdf1",spots[i]);
                    em.persist(pictures[i][j]);
                }
                em.persist(spots[i]);
                em.persist(memberSpots[i]);

                reviews[i] = new Review("content",spots[0]);
                em.persist(reviews[i]);

            }

            em.flush();
            em.clear();

            log.info("spots[0] = {}",spots[0]);  //이거 7임 여기에 리뷰 100개 넣어놨음

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

    }

}

















