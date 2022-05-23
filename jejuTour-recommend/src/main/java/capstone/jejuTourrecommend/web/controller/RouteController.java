package capstone.jejuTourrecommend.web.controller;


import capstone.jejuTourrecommend.domain.Service.RouteService;
import capstone.jejuTourrecommend.web.mainPage.ResultSpotListDto;
import capstone.jejuTourrecommend.web.mainPage.SpotListDto;
import capstone.jejuTourrecommend.web.routePage.ResultFavoriteSpotList;
import capstone.jejuTourrecommend.web.routePage.ResultTopSpot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RouteController {

    private final RouteService routeService;

    @GetMapping("/route/spot/{favoriteId}")
    public ResultFavoriteSpotList favoriteRoute(@PathVariable Long favoriteId){

        Long favoriteIdTest = 3l;

        List<SpotListDto> spotListDtos = routeService.favoriteSpotList(favoriteIdTest);

        return new ResultFavoriteSpotList(200l,true,"성공",spotListDtos);


    }

    @GetMapping("/route/topList/{favoriteId}")
    public ResultTopSpot topList(@PathVariable Long favoriteId){

        Long favoriteIdTest = 3l;

        List list = routeService.recommentSpotList(favoriteIdTest);

        return new ResultTopSpot(200l, true, "성공",list);


    }


}



















