package capstone.jejuTourrecommend.wishList.presentation;


import capstone.jejuTourrecommend.wishList.application.FavoriteFacade;
import capstone.jejuTourrecommend.wishList.presentation.dto.request.RouteForm;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.ResultFavoriteSpotList;
import capstone.jejuTourrecommend.wishList.presentation.dto.response.ResultTopSpot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RouteController {

    private final FavoriteFacade favoriteFacade;

    @GetMapping("/user/route/spot/{favoriteId}")
    public ResultFavoriteSpotList favoriteRoute(@PathVariable("favoriteId") Long favoriteId){

        ResultFavoriteSpotList resultFavoriteSpotList = favoriteFacade.favoriteSpotList(favoriteId);

        return resultFavoriteSpotList;
    }

    @PostMapping("/user/route/topList/{favoriteId}")
    public ResultTopSpot topList(@PathVariable("favoriteId") Long favoriteId, @RequestBody RouteForm routeForm){

        //Todo: 테스트 데이터
        //Long favoriteIdTest = 3l;
        log.info("routeForm.getSpotIdList() = {}", routeForm.getSpotIdList());

        List list = favoriteFacade.recommendSpotList(favoriteId, routeForm);

        return new ResultTopSpot(200l, true, "성공",list);


    }


}



















