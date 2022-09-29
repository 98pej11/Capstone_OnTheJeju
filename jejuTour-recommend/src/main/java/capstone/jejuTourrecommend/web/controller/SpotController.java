package capstone.jejuTourrecommend.web.controller;

import capstone.jejuTourrecommend.Service.SpotService;
import capstone.jejuTourrecommend.web.controller.metaData.DefaultMetaDataBuilder;
import capstone.jejuTourrecommend.web.controller.metaData.MetaDataDirector;
import capstone.jejuTourrecommend.web.login.jwt.JwtTokenProvider;
import capstone.jejuTourrecommend.web.pageDto.mainPage.SpotListMetaDataOp;
import capstone.jejuTourrecommend.web.pageDto.spotPage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
public class SpotController {

    private final SpotService spotService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/user/spot/{spotId}")
    public SpotPageDto spotDetail(@PathVariable("spotId") Long spotId,
                                  @RequestHeader("ACCESS-TOKEN") String accesstoken){

        log.info("spotId = {}",spotId);
        //여기서 spotId를 도메인 클래스 컨버터 사용가능 (jpa 실절)

        //이거 실험용 데이터임 TODO: 실험용 데이터임
        //Long spotIdTest = 8l;
        //String memberEmailTest = "member1@gmail.com";

        //여기서 토큰으로 역할(role) 조회 가능함(header에서 토큰 가져와야함) TODO: 실제 운영할 코드임
        String memberEmail = jwtTokenProvider.getUserPk(accesstoken);

        SpotDetailDto spotDetailDto = spotService.spotPage(spotId, memberEmail);

        return new SpotPageDto(200l,true,"성공",spotDetailDto);

    }

    @GetMapping("/user/spot/review/{spotId}")
    public ReviewListDto reviewPage(@PathVariable("spotId") Long spotId,
                                    @RequestHeader("ACCESS-TOKEN") String accesstoken,
                                    Pageable pageable){
        log.info("spotId = {}",spotId);

        Page<ReviewDto> reviewDtos = spotService.reviewPage(spotId, pageable);

        return new ReviewListDto(200l,true,"성공",reviewDtos);


    }


    @GetMapping("/spot/metaData")
    public SpotMetaDto getMetaData(){

        return getCategoryMetaData();

    }

    @GetMapping("/spot/metaDataOp")
    public SpotListMetaDataOp getMetaDataOp(){

        MetaDataDirector metaDataDirector = new MetaDataDirector(new DefaultMetaDataBuilder());

        return new SpotListMetaDataOp(200l, true, metaDataDirector.categoryMetaData().getMetaDataList());
    }


    //

    private SpotMetaDto getCategoryMetaData() {
        Map map;
        List list = new ArrayList();

        map = new LinkedHashMap();
        map.put("id",1);
        map.put("name","뷰");
        list.add(map);

        map = new LinkedHashMap();
        map.put("id",2);
        map.put("name","가격");
        list.add(map);

        map = new LinkedHashMap();
        map.put("id",3);
        map.put("name","편의시설");
        list.add(map);

        map = new LinkedHashMap();
        map.put("id",4);
        map.put("name","서비스");
        list.add(map);

        return new  SpotMetaDto(200l, true, list);
    }


}
