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


    private final TestService testService;


    @GetMapping("test/spot")
    private SpotDto testSpot(@RequestParam Long spotId){

        SpotDto spotDto = testService.testSpot(spotId);

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
