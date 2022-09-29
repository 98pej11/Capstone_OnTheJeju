package capstone.jejuTourrecommend.web.controller;


import capstone.jejuTourrecommend.Service.SpotListService;
import capstone.jejuTourrecommend.web.login.exceptionClass.UserException;
import capstone.jejuTourrecommend.web.login.jwt.JwtTokenProvider;
import capstone.jejuTourrecommend.web.pageDto.mainPage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SpotListController {


    private final SpotListService spotListService;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/user/spotList/priority")//일단 토큰은 배재하고 검색해보자
    public ResultSpotListDto postSpot(@RequestBody MainPageForm mainPageForm,
                                      Pageable pageable,@RequestHeader("ACCESS-TOKEN") String accesstoken){



        log.info("mainPageForm.getCategory() = {}",mainPageForm.getCategory());
        log.info("mainPageForm.getLocation() = {}",mainPageForm.getLocation());
        log.info("mainPageForm..getUserWeightDto() ={}",mainPageForm.getUserWeight());

        //이거 실험용 데이터임 TODO: 실험용 데이터임
        //String memberEmailTest = "member1@gmail.com";

        //이거 실제 데이터임 TODO: 실제 데이터임
        //여기서 토큰으로 역할(role) 조회 가능함(header에서 토큰 가져와야함)
        String memberEmail = jwtTokenProvider.getUserPk(accesstoken);


        log.info("memberEmail = {}",memberEmail);
        log.info("pageable = {}",pageable);

        ResultSpotListDto resultSpotListDto = spotListService
                .postSpotList(mainPageForm, memberEmail, pageable);

        return resultSpotListDto;

    }

    @PostMapping("/user/spotList/search")//일단 토큰은 배재하고 검색해보자
    public ResultSpotListDto searchSpotListContains(@RequestBody SearchForm searchForm,
                                      Pageable pageable,@RequestHeader("ACCESS-TOKEN") String accesstoken) {



        log.info("spotName = {}",searchForm.getSpotName());

        if(!StringUtils.hasText(searchForm.getSpotName())){
            throw new UserException("관광지 이름에 빈 문자열이 왔습니다");
        }

        String memberEmail = jwtTokenProvider.getUserPk(accesstoken);

        ResultSpotListDto resultSpotListDto = spotListService.searchSpotListContains(memberEmail,searchForm.getSpotName(), pageable);

        return resultSpotListDto;
    }


    @GetMapping("/spotList/metaData")
    public SpotListMetaDto getMetaData(){


        SpotListMetaDto spotListMetaDto = getSpotListMetaDto();


        return spotListMetaDto;


    }

    private SpotListMetaDto getSpotListMetaDto() {
        Map map;
        List list = new ArrayList();

        map = new LinkedHashMap();
        map.put("id",1);
        map.put("name","전체");
        list.add(map);

        map = new LinkedHashMap();
        map.put("id",2);
        map.put("name","뷰");
        list.add(map);

        map = new LinkedHashMap();
        map.put("id",3);
        map.put("name","가격");
        list.add(map);

        map = new LinkedHashMap();
        map.put("id",4);
        map.put("name","편의시설");
        list.add(map);

        map = new LinkedHashMap();
        map.put("id",5);
        map.put("name","서비스");
        list.add(map);

//
        Map map1;
        List list1 = new ArrayList();

        map1 = new LinkedHashMap();
        map1.put("id",6);
        map1.put("name","전체");
        list1.add(map1);

        map1 = new LinkedHashMap();
        map1.put("id",7);
        map1.put("name","북부");
        list1.add(map1);

        map1 = new LinkedHashMap();
        map1.put("id",8);
        map1.put("name","남부");
        list1.add(map1);

        map1 = new LinkedHashMap();
        map1.put("id",9);
        map1.put("name","서부");
        list1.add(map1);

        map1 = new LinkedHashMap();
        map1.put("id",10);
        map1.put("name","동부");
        list1.add(map1);

        SpotListMetaDto spotListMetaDto = new SpotListMetaDto(200l, true, list, list1);
        return spotListMetaDto;
    }


}

/**
 map1 = new LinkedHashMap();
 map1.put("id",5);
 map1.put("name","제주시");
 list1.add(map1);

 map1 = new LinkedHashMap();
 map1.put("id",6);
 map1.put("name","애월읍");
 list1.add(map1);

 map1 = new LinkedHashMap();
 map1.put("id",7);
 map1.put("name","한림읍");
 list1.add(map1);

 map1 = new LinkedHashMap();
 map1.put("id",8);
 map1.put("name","한경면");
 list1.add(map1);

 map1 = new LinkedHashMap();
 map1.put("id",9);
 map1.put("name","조천읍");
 list1.add(map1);

 map1 = new LinkedHashMap();
 map1.put("id",10);
 map1.put("name","구좌읍");
 list1.add(map1);

 map1 = new LinkedHashMap();
 map1.put("id",11);
 map1.put("name","대정읍");
 list1.add(map1);

 map1 = new LinkedHashMap();
 map1.put("id",12);
 map1.put("name","안덕면");
 list1.add(map1);

 map1 = new LinkedHashMap();
 map1.put("id",13);
 map1.put("name","서귀포");
 list1.add(map1);

 map1 = new LinkedHashMap();
 map1.put("id",14);
 map1.put("name","남원읍");
 list1.add(map1);

 map1 = new LinkedHashMap();
 map1.put("id",15);
 map1.put("name","표선면");
 list1.add(map1);

 map1 = new LinkedHashMap();
 map1.put("id",16);
 map1.put("name","성산읍");
 list1.add(map1);

 map1 = new LinkedHashMap();
 map1.put("id",17);
 map1.put("name","우도면");
 list1.add(map1);

 map1 = new LinkedHashMap();
 map1.put("id",18);
 map1.put("name","추자면");
 list1.add(map1);
 */





