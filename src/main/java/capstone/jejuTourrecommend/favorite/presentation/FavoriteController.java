package capstone.jejuTourrecommend.favorite.presentation;


import capstone.jejuTourrecommend.authentication.LoginUser;
import capstone.jejuTourrecommend.common.GlobalDto;
import capstone.jejuTourrecommend.domain.Member;
import capstone.jejuTourrecommend.favorite.domain.dto.FavoriteDto;
import capstone.jejuTourrecommend.favorite.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.favorite.domain.service.FavoriteService;
import capstone.jejuTourrecommend.favorite.presentation.dto.request.FavoriteForm;
import capstone.jejuTourrecommend.favorite.presentation.dto.request.FavoriteNewForm;
import capstone.jejuTourrecommend.favorite.presentation.dto.response.FavoriteListFinalDto;
import capstone.jejuTourrecommend.favorite.presentation.dto.response.NewFavoriteListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * 선택한 관광지를 선태한 위시리스트에 추가
     * 선택한 관광지 정보, 사용자 정보, 위시리스트 정보 필요
     *
     * @param favoriteForm
     * @return
     */
    @PostMapping("/user/favorite/form")
    public GlobalDto postFavoriteForm(//@RequestHeader("ACCESS-TOKEN") String accesstoken,
                                      @RequestBody FavoriteForm favoriteForm) {


        //Todo: 테스트용 데이터
        //String memberEmailTest="member1@gmail.com";

        //여기서 토큰으로 역할(role) 조회 가능함(header에서 토큰 가져와야함)
        //jwtTokenProvider.getUserPk(jwtTokenProvider.createToken(memberEmail,"ROLE_USER"));

        //Todo: 테스트용 데이터
        //Long spotIdTest = 8l;
        //Long favoriteIdTest = 3l;

        favoriteService.postFavoriteForm(favoriteForm);

        return new GlobalDto(200l, true, "성공");


    }//나 여기서 get post 의미를 정보 수정의 의미로 두었음 반화값은 유무가 아니라

    /**
     * 새로운 위시 리스트를 만들고 해당 관광지 넣기
     * 선택한 관광지 정보, 사용자 정보, 위시리스트 이름 필요
     *
     * @param form
     * @return
     */
    @PostMapping("/user/favorite/new")
    public NewFavoriteListDto newFavoriteList(
            @RequestBody FavoriteNewForm form,
            @LoginUser Member member) {


        Long spotId = form.getSpotId();
        String favoriteName = form.getFavoriteName();


        //여기서 관광지 정보 유무로 나눠야 함
        //있으면 위시리스트 생성 및 관광지 추가, 없으면 그냥 위시리스트 생성

        //Todo: Long spotId = 8l;
        //Long spotIdTest = null;
        //String favoriteNameTest = "1일차 관광지";
        //테스트용 데이터
        //String memberEmailTest="member1@gmail.com";

        //여기서 토큰으로 역할(role) 조회 가능함(header에서 토큰 가져와야함)
        //jwtTokenProvider.getUserPk(jwtTokenProvider.createToken(memberEmailTest,"ROLE_USER"));


        FavoriteDto favoriteDto = favoriteService.newFavoriteList(member, spotId, favoriteName);

        return new NewFavoriteListDto(200l, true, "성공", favoriteDto);
    }

    /**
     * 위시 리스트 페이지
     * 사용자 정보 필요
     *
     * @param pageable
     * @return
     */
    @GetMapping("/user/favoriteList")
    public FavoriteListFinalDto favoriteList(Pageable pageable, @LoginUser Member member) {

        //여기서 토큰으로 역할(role) 조회 가능함(header에서 토큰 가져와야함)


        //Todo: 테스트용 데이터
        //String memberEmailTest="member1@gmail.com";

        //여기서 토큰으로 역할(role) 조회 가능함(header에서 토큰 가져와야함)
        //jwtTokenProvider.getUserPk(jwtTokenProvider.createToken(memberEmail,"ROLE_USER"));

        Page<FavoriteListDto> favoriteList = favoriteService.getFavoriteList(member.getId(), pageable);

        return new FavoriteListFinalDto(200l, true, "성공,", favoriteList);

    }


    /**
     * 위시 리스트 삭제하기
     * 해당 위시리스트 정보 필요
     *
     * @param favoriteId
     * @return
     */
    @DeleteMapping("/user/favoriteList/{favoriteId}")
    public GlobalDto deleteFavoriteList(@PathVariable("favoriteId") Long favoriteId) {

        //Todo: 테스트용 데이터
        //Long memberId;//이거 없어도 될것같음
        //Long favoriteIdTest = 3l;

        favoriteService.deleteFavoriteList(favoriteId);

        return new GlobalDto(200l, true, "성공");

    }


    @DeleteMapping("/user/favoriteList/deleteSpot")
    public GlobalDto deleteSpotInFavoriteList(@RequestParam Long favoriteId,
                                              @RequestParam Long spotId) {
        //3,
        favoriteService.deleteSpotInFavoriteList(favoriteId, spotId);

        return new GlobalDto(200l, true, "성공");
    }


}












