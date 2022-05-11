package capstone.jejuTourrecommend.web.controller;


import capstone.jejuTourrecommend.domain.Service.TestService;
import capstone.jejuTourrecommend.web.spotPage.PictureDto;
import capstone.jejuTourrecommend.web.spotPage.ReviewDto;
import capstone.jejuTourrecommend.web.spotPage.ScoreDto;
import capstone.jejuTourrecommend.web.spotPage.SpotDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TestController {

    //mysql 하고 연결할때 initData 클래스 안에 있는 걸 모두 주석 처리하고 실행할 것
    //내가 실험 용으로 데이터로 넣으거라 mysql 데이터와 충돌 일어 날 수 있음


    private final TestService testService;

    @GetMapping("test/spot")
    private SpotDto testSpot(@RequestParam Long spotId){

        SpotDto spotDto = testService.testSpot(spotId);

        log.info("spotDot = {}",spotDto );

        return spotDto;
    }

    @GetMapping("test/review")
    private ReviewDto testReview(@RequestParam Long reviewId){

        ReviewDto reviewDto = testService.testReview(reviewId);

        return reviewDto;
    }

    @GetMapping("test/picture")
    private PictureDto testPicture(@RequestParam Long pictureId){

        PictureDto pictureDto = testService.testPicture(pictureId);

        return pictureDto;
    }

    @GetMapping("test/score")
    private ScoreDto testScore(@RequestParam Long scoreId){

        ScoreDto scoreDto = testService.testScore(scoreId);

        return scoreDto;
    }

}
