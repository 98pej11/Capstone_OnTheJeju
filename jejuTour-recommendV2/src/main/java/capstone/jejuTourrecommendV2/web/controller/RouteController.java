package capstone.jejuTourrecommendV2.web.controller;


import capstone.jejuTourrecommendV2.domain.Service.RouteService;
import capstone.jejuTourrecommendV2.web.pageDto.routePage.ResultFavoriteSpotList;
import capstone.jejuTourrecommendV2.web.pageDto.routePage.ResultTopSpot;
import capstone.jejuTourrecommendV2.web.pageDto.routePage.RouteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RouteController {

    private final RouteService routeService;

    @GetMapping("/user/route/spot/{favoriteId}")
    public ResultFavoriteSpotList favoriteRoute(@PathVariable("favoriteId") Long favoriteId){

        //Todo: 테스트 데이터
        //Long favoriteIdTest = 3l;

        ResultFavoriteSpotList resultFavoriteSpotList = routeService.favoriteSpotList(favoriteId);

        return resultFavoriteSpotList;
    }

    @PostMapping("/user/route/topList/{favoriteId}")
    public ResultTopSpot topList(@PathVariable("favoriteId") Long favoriteId, @RequestBody RouteForm routeForm){

        //Todo: 테스트 데이터
        //Long favoriteIdTest = 3l;
        log.info("routeForm.getSpotIdList() = {}", routeForm.getSpotIdList());

        List list = routeService.recommentSpotList(favoriteId, routeForm);

        return new ResultTopSpot(200l, true, "성공",list);


    }


}



















