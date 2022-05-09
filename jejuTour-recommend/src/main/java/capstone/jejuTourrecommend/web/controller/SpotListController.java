package capstone.jejuTourrecommend.web.controller;


import capstone.jejuTourrecommend.domain.Category;
import capstone.jejuTourrecommend.domain.Location;
import capstone.jejuTourrecommend.domain.Service.SpotListService;
import capstone.jejuTourrecommend.repository.SpotRepository;
import capstone.jejuTourrecommend.web.login.jwt.JwtTokenProvider;
import capstone.jejuTourrecommend.web.mainPage.MainPageForm;
import capstone.jejuTourrecommend.web.mainPage.ResultSpotListDto;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SpotListController {


    private final SpotListService spotListService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/spotList")
    public ResultSpotListDto getSpot(@RequestBody MainPageForm mainPageForm, Pageable pageable){

        //아래 두줄은 내가 실험 넣은 코드임
        Location location = null;
        Category category = null;
        log.info("location = {} ",location);
        log.info("category = {} ",category);


        ResultSpotListDto spotList = spotListService.getSpotList(mainPageForm, pageable);
        return spotList;

//        mainPageForm.setPage(0);
//        mainPageForm.setPage(10);
//        PageRequest pageRequest = PageRequest.of(mainPageForm.getPage(), mainPageForm.getSize());


    }


    //프런트에서 객체단위로 줄수 있나?//그냥 변수 이름만 맞추면 되나?
    @PostMapping("/spotList/priority")//일단 토큰은 배재하고 검색해보자
    public ResultSpotListDto postSpot(@RequestBody MainPageForm mainPageForm,
                                      Pageable pageable,@RequestHeader("ACCESS-TOKEN") String accesstoken){


        String memberEmailTest = "member1@gmail.com";

        //여기서 토큰으로 역할(role) 조회 가능함(header에서 토큰 가져와야함)
        String memberEmail = jwtTokenProvider.getUserPk(accesstoken);


        log.info("memberId = {}",memberEmailTest);
        log.info("pageable = {}",pageable);

        ResultSpotListDto resultSpotListDto = spotListService
                .postSpotList(mainPageForm, memberEmail, pageable);

        return resultSpotListDto;

    }




}







