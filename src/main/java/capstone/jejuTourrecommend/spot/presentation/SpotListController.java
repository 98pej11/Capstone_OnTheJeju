package capstone.jejuTourrecommend.spot.presentation;


import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.common.metaDataBuilder.DefaultMetaDataBuilder;
import capstone.jejuTourrecommend.common.metaDataBuilder.MetaData;
import capstone.jejuTourrecommend.common.metaDataBuilder.MetaDataDirector;
import capstone.jejuTourrecommend.spot.domain.mainSpot.service.SpotListService;
import capstone.jejuTourrecommend.spot.presentation.request.MainPageForm;
import capstone.jejuTourrecommend.spot.presentation.request.SearchForm;
import capstone.jejuTourrecommend.spot.presentation.response.ResultSpotListDto;
import capstone.jejuTourrecommend.spot.presentation.response.SpotListMetaDataOp;
import capstone.jejuTourrecommend.spot.presentation.response.SpotListMetaDto;
import capstone.jejuTourrecommend.authentication.LoginUser;
import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SpotListController {


    private final SpotListService spotListService;


    @PostMapping("/user/spotList/priority")//일단 토큰은 배재하고 검색해보자
    public ResultSpotListDto postSpot(@RequestBody MainPageForm mainPageForm,
                                      Pageable pageable,
                                      @LoginUser Member member){

        //userDetails.getUsername();

        log.info("member = {}", member.toString());
        log.info("member.getUsername() = {}", member.getUsername());
        log.info("member.getPassword() = {}", member.getPassword());



        log.info("mainPageForm.getCategory() = {}",mainPageForm.getCategory());
        log.info("mainPageForm.getLocation() = {}",mainPageForm.getLocation());
        log.info("mainPageForm..getUserWeightDto() ={}",mainPageForm.getUserWeight());

        //이거 실험용 데이터임 TODO: 실험용 데이터임
        //String memberEmailTest = "member1@gmail.com";

        //이거 실제 데이터임 TODO: 실제 데이터임
        //여기서 토큰으로 역할(role) 조회 가능함(header에서 토큰 가져와야함)
//        String memberEmail = jwtTokenProvider.getUserPk(accesstoken);
//
//
//        log.info("memberEmail = {}",memberEmail);
//        log.info("pageable = {}",pageable);

        ResultSpotListDto resultSpotListDto = spotListService
                .postSpotList(mainPageForm, member.getId(), pageable);

        return resultSpotListDto;

    }

    @PostMapping("/user/spotList/search")//일단 토큰은 배재하고 검색해보자
    public ResultSpotListDto searchSpotListContains(@RequestBody SearchForm searchForm,
                                      Pageable pageable,
                                                    @LoginUser Member member) {

        log.info("spotName = {}",searchForm.getSpotName());

        if(!StringUtils.hasText(searchForm.getSpotName())){
            throw new UserException("관광지 이름에 빈 문자열이 왔습니다");
        }

        ResultSpotListDto resultSpotListDto = spotListService.searchSpotListContains(member.getId(), searchForm.getSpotName(), pageable);

        return resultSpotListDto;
    }


    @GetMapping("/spotList/metaData")
    public SpotListMetaDto getMetaData(){


        SpotListMetaDto spotListMetaDto = getSpotListMetaDto();


        return spotListMetaDto;


    }

    @GetMapping("/spotList/metaDataOp")
    public SpotListMetaDataOp getMetaData1(){

        MetaDataDirector metaDataDirector = new MetaDataDirector(new DefaultMetaDataBuilder());
        metaDataDirector.categoryMetaData();
        MetaData metaData2 = metaDataDirector.locationMetaData();

        return new SpotListMetaDataOp(200l, true, metaData2.getMetaDataList());


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




