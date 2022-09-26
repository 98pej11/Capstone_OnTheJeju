package capstone.jejuTourrecommend.repository;

import capstone.jejuTourrecommend.domain.*;
import capstone.jejuTourrecommend.web.pageDto.favoritePage.FavoriteListDto;
import capstone.jejuTourrecommend.web.pageDto.favoritePage.OptimizationFavoriteListDto;
import capstone.jejuTourrecommend.web.pageDto.mainPage.OptimizationSpotListDto;
import capstone.jejuTourrecommend.web.pageDto.mainPage.ResultSpotListDto;
import capstone.jejuTourrecommend.web.pageDto.mainPage.SpotListDto;
import capstone.jejuTourrecommend.web.pageDto.mainPage.UserWeightDto;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@SpringBootTest
@Transactional
//@Commit
class SpotRepositoryTest {


    @Autowired
    SpotRepository spotRepository;
    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    //@BeforeEach
    public void before() {
        Random random = new Random();

        Score[] scores = new Score[100];
        Spot[] spots = new Spot[100];

        Picture[][] pictures = new Picture[100][3];

        for (int i = 0; i < 100; i++) { //지역하고 score만

            if (0 <= i && i < 25) {
                spots[i] = new Spot(Location.Aewol_eup, createScore(scores, i));
            }
            if (25 <= i && i < 50) {
                spots[i] = new Spot(Location.Aewol_eup, createScore(scores, i));
            }
            if (50 <= i && i < 75) {
                spots[i] = new Spot(Location.Andeok_myeon, createScore(scores, i));
            }
            if (75 <= i && i < 100) {
                spots[i] = new Spot(Location.Andeok_myeon, createScore(scores, i));
            }

            for (int j = 0; j < 3; j++) {
                pictures[i][j] = new Picture("asdf1", spots[i]);
                em.persist(pictures[i][j]);
            }


            //spots[i].setScore(scores[i]);
            //log.info("spots[i].getScore().toString() = {}",spots[i].getScore().toString());
            //log.info("spots[i].getPictures().toArray() = {}",spots[i].getPictures().toArray());
            em.persist(spots[i]);
        }


        em.flush();
        em.clear();

        for (Spot spot : spots) {
            System.out.println("spot = " + spot);
        }
    }

    @BeforeEach
    public void before1() {

        Member member1 = new Member("wwntn", "member1@gmail.com");
        em.persist(member1);

        MemberSpot[] memberSpots = new MemberSpot[5];
        Score[] scores = new Score[5];
        Spot[] spots = new Spot[5];
        Picture[][] pictures = new Picture[5][2];

        for (int i = 0; i < 5; i++) { //지역하고 score만

            spots[i] = new Spot(Location.Andeok_myeon, createScore(scores, i));
            em.persist(spots[i]);
            memberSpots[i] = new MemberSpot(0d, member1, spots[i]);
            em.persist(memberSpots[i]);

            for (int j = 0; j < 2; j++) {
                pictures[i][j] = new Picture("asdf1", spots[i]);
                em.persist(pictures[i][j]);
            }


        }
        em.flush();
        em.clear();

        for (Spot spot : spots) {
            System.out.println("spot = " + spot);
        }
        for (MemberSpot memberSpot : memberSpots) {
            System.out.println("memberSpot = " + memberSpot);
        }

        for (Picture[] picture : pictures) {
            for (Picture picture1 : picture) {
                System.out.println("picture = " + picture1);
            }
        }
    }

    public Score createScore(Score[] scores, int i) {
        Random random = new Random();
        scores[i] = new Score(
                random.nextDouble() * 10, random.nextDouble() * 10,
                random.nextDouble() * 10, random.nextDouble() * 10,
                random.nextDouble() * 10, random.nextDouble() * 10,
                random.nextDouble() * 10, random.nextDouble() * 10,
                random.nextDouble() * 10);
        em.persist(scores[i]);//여기서 이거 한해도 되는 이유는
        // cascade = CascadeType.ALL를 해놓아서 그런거임, 원래는 넣어줘야 함
        return scores[i];
    }


    @Test
    public void SpotLocationCategoryTest() throws Exception {


        PageRequest pageRequest = PageRequest.of(0, 100);


        List allList = Arrays.asList(Location.Jeju_si,Location.Aewol_eup,Location.Hallim_eup,
                Location.Hangyeong_myeon,Location.Jocheon_eup,Location.Gujwa_eup,
                Location.Daejeong_eup,Location.Andeok_myeon,Location.Seogwipo_si,
                Location.Namwon_eup,Location.Pyoseon_myeon,Location.Seongsan_eup);
        List northList = Arrays.asList(Location.Jeju_si, Location.Aewol_eup, Location.Hallim_eup);
        List eastList = Arrays.asList(Location.Hangyeong_myeon, Location.Jocheon_eup, Location.Gujwa_eup);
        List westList = Arrays.asList(Location.Daejeong_eup, Location.Andeok_myeon, Location.Seogwipo_si);
        List southList = Arrays.asList(Location.Namwon_eup, Location.Pyoseon_myeon, Location.Seongsan_eup);


        long before1 = System.currentTimeMillis();
        Page<SpotListDto> results = spotRepository.
                searchSpotByLocationAndCategory(westList, Category.VIEW, pageRequest);
        long after1 = System.currentTimeMillis();

        System.out.println("after1-before1 = " +  (after1 - before1));



        for (SpotListDto result : results) {
            System.out.println("result.getSpotId() = " + result.getSpotId());
        }


        ResultSpotListDto r = new ResultSpotListDto(200l, true, "성공", results);
        System.out.println("r = " + r);
    }


    //93 89 100
    //81 75 77

    @Test
    public void OptimizationSpotLocationCategoryTest() throws Exception {

        String memberEmail = "member1@gmail.com";

        Optional<Member> optionByEmail = memberRepository.findOptionByEmail(memberEmail);

        PageRequest pageRequest = PageRequest.of(0, 100);


        List allList = Arrays.asList(Location.Jeju_si,Location.Aewol_eup,Location.Hallim_eup,
                Location.Hangyeong_myeon,Location.Jocheon_eup,Location.Gujwa_eup,
                Location.Daejeong_eup,Location.Andeok_myeon,Location.Seogwipo_si,
                Location.Namwon_eup,Location.Pyoseon_myeon,Location.Seongsan_eup);
        List northList = Arrays.asList(Location.Jeju_si, Location.Aewol_eup, Location.Hallim_eup);
        List eastList = Arrays.asList(Location.Hangyeong_myeon, Location.Jocheon_eup, Location.Gujwa_eup);
        List westList = Arrays.asList(Location.Daejeong_eup, Location.Andeok_myeon, Location.Seogwipo_si);
        List southList = Arrays.asList(Location.Namwon_eup, Location.Pyoseon_myeon, Location.Seongsan_eup);


        em.flush();
        em.clear();
        long before2 = System.currentTimeMillis();
        Page<OptimizationSpotListDto> results2 = spotRepository.
                optimizationSearchSpotByLocationAndCategory(optionByEmail.get().getId(), westList, Category.VIEW, pageRequest);
        long after2 = System.currentTimeMillis();



        //em.clear();

        System.out.println("after2-before2 = " +  (after2 - before2)); //-> ToDo: 10배 정도의 성능 최적화 됨


    }

    @Test
    public void searchSpotByUserPriority() throws Exception {
        //given

        String memberEmail = "member1@gmail.com";

        Optional<Member> optionByEmail = memberRepository.findOptionByEmail(memberEmail);

        PageRequest pageRequest = PageRequest.of(0, 100);

        List allList = Arrays.asList(Location.Jeju_si,Location.Aewol_eup,Location.Hallim_eup,
                Location.Hangyeong_myeon,Location.Jocheon_eup,Location.Gujwa_eup,
                Location.Daejeong_eup,Location.Andeok_myeon,Location.Seogwipo_si,
                Location.Namwon_eup,Location.Pyoseon_myeon,Location.Seongsan_eup);
        List northList = Arrays.asList(Location.Jeju_si, Location.Aewol_eup, Location.Hallim_eup);
        List eastList = Arrays.asList(Location.Hangyeong_myeon, Location.Jocheon_eup, Location.Gujwa_eup);
        List westList = Arrays.asList(Location.Daejeong_eup, Location.Andeok_myeon, Location.Seogwipo_si);
        List southList = Arrays.asList(Location.Namwon_eup, Location.Pyoseon_myeon, Location.Seongsan_eup);



        long before1 = System.currentTimeMillis();
        Page<SpotListDto> result = spotRepository.searchSpotByUserPriority(optionByEmail.get().getId(), westList,
                new UserWeightDto(1d, 4d, 1d, 1d)
                , pageRequest);
        long after1 = System.currentTimeMillis();

        System.out.println("after1-before1 = " +  (after1 - before1));


        List<MemberSpot> resultList = em.createQuery
                ("select s from MemberSpot s", MemberSpot.class).getResultList();

        for (MemberSpot memberSpot : resultList) {
            log.info("memberSpot = {} ", memberSpot);
        }

        em.flush();

        em.clear();

    }

    //97 93 91
    //110 108 110
    @Test
    public void OptimizationSearchSpotByUserPriority() throws Exception {
        //given

        String memberEmail = "member1@gmail.com";

        Optional<Member> optionByEmail = memberRepository.findOptionByEmail(memberEmail);

        PageRequest pageRequest = PageRequest.of(0, 100);

        List allList = Arrays.asList(Location.Jeju_si,Location.Aewol_eup,Location.Hallim_eup,
                Location.Hangyeong_myeon,Location.Jocheon_eup,Location.Gujwa_eup,
                Location.Daejeong_eup,Location.Andeok_myeon,Location.Seogwipo_si,
                Location.Namwon_eup,Location.Pyoseon_myeon,Location.Seongsan_eup);
        List northList = Arrays.asList(Location.Jeju_si, Location.Aewol_eup, Location.Hallim_eup);
        List eastList = Arrays.asList(Location.Hangyeong_myeon, Location.Jocheon_eup, Location.Gujwa_eup);
        List westList = Arrays.asList(Location.Daejeong_eup, Location.Andeok_myeon, Location.Seogwipo_si);
        List southList = Arrays.asList(Location.Namwon_eup, Location.Pyoseon_myeon, Location.Seongsan_eup);



        long before2 = System.currentTimeMillis();
        Page<OptimizationSpotListDto> optimizationSpotListDtos = spotRepository.optimizationSearchSpotByUserPriority(optionByEmail.get().getId(), westList,
                new UserWeightDto(1d, 4d, 1d, 1d)
                , pageRequest);
        long after2 = System.currentTimeMillis();

        System.out.println("after2-before2 = " +  (after2 - before2)); //->ToDo: 7배의 성능 최적화가 됨



        System.out.println("optimizationSpotListDtos.getContent() = " + optimizationSpotListDtos.getContent());



    }

    @Test
    public void searchSpotName() throws Exception{
        //given
        Spot spot1 = new Spot("가우리는");
        Spot spot2 = new Spot("우리는");
        Spot spot3 = new Spot("가우리");
        Spot spot4 = new Spot("가우리 는");
        Spot spot5 = new Spot("가 우리는");
        Spot spot6 = new Spot("가 우리 는");

        em.persist(spot1);
        em.persist(spot2);
        em.persist(spot3);
        em.persist(spot4);
        em.persist(spot5);
        em.persist(spot6);

        int page = 0;
        int size = 3;

        PageRequest pageRequest = PageRequest.of(page, size);

        //when
        //spotRepository.findByNameLike("%우리%").stream().map(spot -> new SpotListDto(spo))
        Page<SpotListDto> spotListDtos = spotRepository.searchBySpotNameContains("우리", pageRequest);


        //then
        Assertions.assertThat(spotListDtos.getContent().size()).isEqualTo(3);


    }

}










