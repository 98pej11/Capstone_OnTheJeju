package capstone.jejuTourrecommend.domain.Service;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.favorite.domain.Favorite;
import capstone.jejuTourrecommend.favorite.domain.FavoriteSpot;
import capstone.jejuTourrecommend.favorite.domain.service.FavoriteService;
import capstone.jejuTourrecommend.favorite.domain.dto.FavoriteDto;
import capstone.jejuTourrecommend.favorite.infrastructure.repository.FavoriteJpaRepository;
import capstone.jejuTourrecommend.favorite.infrastructure.repository.FavoriteSpotJpaRepository;
import capstone.jejuTourrecommend.favorite.infrastructure.repository.FavoriteSpotQuerydslRepository;
import capstone.jejuTourrecommend.favorite.presentation.dto.request.FavoriteForm;
import capstone.jejuTourrecommend.favorite.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.detailSpot.Picture;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot.SpotJpaRepository;
import capstone.jejuTourrecommend.authentication.infrastructure.respository.MemberJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class FavoriteServiceTest {


    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    FavoriteJpaRepository favoriteJpaRepository;
    @Autowired
    SpotJpaRepository spotJpaRepository;
    @Autowired
    FavoriteSpotJpaRepository favoriteSpotJpaRepository;
    @Autowired
    FavoriteSpotQuerydslRepository favoriteSpotQuerydslRepository;
    @Autowired
    FavoriteService favoriteService;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void testData() {

        Member member = new Member("leoJoo", "em@naver.com", "1234");
        em.persist(member);

        Favorite favorite1 = new Favorite("1일차", member);
        Favorite favorite2 = new Favorite("2일차", member);
        Favorite favorite3 = new Favorite("3일차", member);
        Favorite favorite4 = new Favorite("4일차", member);
        Favorite favorite5 = new Favorite("5일차", member);
        em.persist(favorite1);
        em.persist(favorite2);
        em.persist(favorite3);
        em.persist(favorite4);
        em.persist(favorite5);
        em.persist(favorite5);
        log.info("favorite1 = {}", favorite1);//1
        log.info("favorite5 = {}", favorite5);//5

        Spot[] spots = new Spot[15];
        for (int i = 0; i < 15; i++) {
            spots[i] = new Spot("관광지 " + Integer.toString(i));

            em.persist(spots[i]);
        }
        log.info("spot0 = {}", spots[0]);  //7번임
        log.info("spot14 = {}", spots[14]);  //15번임

        FavoriteSpot[] favoriteSpots = new FavoriteSpot[15];

        Picture[] pictures = new Picture[15];

        for (int i = 0; i < 5; i++) {
            favoriteSpots[i] = new FavoriteSpot(favorite1, spots[i]);
            em.persist(favoriteSpots[i]);
            pictures[i] = new Picture("숫자" + i, spots[i]);
            em.persist(pictures[i]);
        }
        for (int i = 5; i < 10; i++) {
            favoriteSpots[i] = new FavoriteSpot(favorite2, spots[i]);
            em.persist(favoriteSpots[i]);
            pictures[i] = new Picture("숫자" + i, spots[i]);
            em.persist(pictures[i]);
        }
        for (int i = 10; i < 15; i++) {
            favoriteSpots[i] = new FavoriteSpot(favorite3, spots[i]);
            em.persist(favoriteSpots[i]);
            pictures[i] = new Picture("숫자" + i, spots[i]);
            em.persist(pictures[i]);
        }


    }

    /**
     * 선택한 관광지를 선태한 위시리스트에 추가
     * String memberEmail, Long spotId, Long favoriteId
     * 사용자의 위시리스트 목록 "폼" 보여주기
     * @throws Exception
     */
    @Test
    public void postFavoriteFormTest() throws Exception {

        Spot spot = spotJpaRepository.findOptionByName("관광지 0").orElseThrow(() -> new UserException("해당 이름의 관광지가 없습니다."));

        Member member = memberJpaRepository.findOptionByEmail("em@naver.com").orElseThrow(() -> new UserException("해당 이름의 회원이 없습니다."));
        Favorite favorite = favoriteJpaRepository.findOptionByNameAndMemberId("5일차", member.getId()).orElseThrow(() -> new UserException("해당 이름의 위시리스트가 없습니다."));

        //given
        //String memberEmail = "em@naver.com";
        //7,2 중복된 관관지 존재, 새로운 추가 21,6
        FavoriteForm favoriteForm = new FavoriteForm();

        favoriteForm.setSpotId(spot.getId());
        favoriteForm.setFavoriteId(favorite.getId());
        Long spotId = spot.getId();
        Long favoriteId = favorite.getId();

        //when
        favoriteService.postFavoriteForm(favoriteForm);

        Optional<FavoriteSpot> result = favoriteSpotJpaRepository.findOptionBySpotIdAndFavoriteId(spotId, favoriteId);

        assertThat(result).isNotEmpty();

//

        //then
    }

    @Test
    public void getFavoriteListTest() throws Exception {
        //given

        //when
        String memberEmail = "em@naver.com";
        //Favorite favorite2 = new Favorite("2일차",m)

        //Spot spot = new Spot("테스트")

        Optional<Member> optionByEmail = memberJpaRepository.findOptionByEmail(memberEmail);

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<FavoriteListDto> favoriteList = favoriteService.getFavoriteList(optionByEmail.get().getId(), pageRequest);


        List<FavoriteListDto> content = favoriteList.getContent();
        log.info("favoriteListContent = {}", content);

        assertThat(content.size()).isEqualTo(5);
        assertThat(favoriteList.getTotalElements()).isEqualTo(5);

    }


    /**
     * 새로운 위시 리스트를 만들고 해당 관광지 넣기
     * String memberEmail, Long spotId, String favoriteName
     * @throws Exception
     */
    @Test
    public void newFavoriteListTest() throws Exception {
        //given
        String memberEmail = "em@naver.com";

        Spot spot = spotJpaRepository.findOptionByName("관광지 14").orElseThrow(() -> new UserException("해당 이름의 관광지가 없습니다."));
        Long spotId = spot.getId(); //spots[14]);  //15번임

        String favoriteName = "새로운 위시리스트1";

        Member member = memberJpaRepository.findOptionByEmail(memberEmail)
                .orElseThrow(() -> new UserException("가입되지 않은 E-MAIL 입니다."));

        //when

        FavoriteDto favoriteDto = favoriteService.newFavoriteList(member, spotId, favoriteName);

        Optional<Favorite> favorite = favoriteJpaRepository.findOptionByNameAndMemberId(favoriteName, member.getId());

        Optional<FavoriteSpot> result = favoriteSpotJpaRepository.findOptionBySpotIdAndFavoriteId(spotId, favorite.get().getId());



        //then
        assertThat(favorite).isNotEmpty();
        assertThat(result).isNotEmpty();
        log.info("favoriteDto = {}", favoriteDto);
        assertThat(favoriteDto).isNotNull();
    }


    /**
     * 위시 리스트 삭제하기
     * @throws Exception
     */
    @Test
    public void deleteFavoriteListTest() throws Exception {
        //given
        Member member = memberJpaRepository.findOptionByEmail("em@naver.com").orElseThrow(() -> new UserException("해당 이름의 회원이 없습니다."));
        Favorite favorite = favoriteJpaRepository.findOptionByNameAndMemberId("5일차", member.getId()).orElseThrow(() -> new UserException("해당 이름의 위시리스트가 없습니다."));

        Long favoriteId = favorite.getId();

        //when
        favoriteService.deleteFavoriteList(favoriteId);
        Optional<Favorite> result = favoriteJpaRepository.findOptionById(favoriteId);

        //then
        assertThat(result).isEmpty();
    }



}






















