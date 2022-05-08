package capstone.jejuTourrecommend.domain.Service;


import capstone.jejuTourrecommend.repository.*;
import capstone.jejuTourrecommend.web.spotPage.PictureDto;
import capstone.jejuTourrecommend.web.spotPage.ReviewDto;
import capstone.jejuTourrecommend.web.spotPage.ScoreDto;
import capstone.jejuTourrecommend.web.spotPage.SpotDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TestService {

    private final SpotRepository spotRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final PictureRepository pictureRepository;
    private final MemberSpotRepository memberSpotRepository;
    private final ScoreRepository scoreRepository;


    public SpotDto testSpot(Long spotId){

        Optional<SpotDto> spotDto = spotRepository.findOptionById(spotId).map(s -> new SpotDto(s));

        return spotDto.get();

    }

    public ReviewDto testReview(Long reviewId){

        Optional<ReviewDto> reviewDto = reviewRepository.findById(reviewId).map(r -> new ReviewDto(r));

        return reviewDto.get();

    }

    public PictureDto testPicture(Long pictureId){

        Optional<PictureDto> pictureDto = pictureRepository.findById(pictureId).map(p -> new PictureDto(p));

        return pictureDto.get();

    }


    public ScoreDto testScore(Long scoreId){

        Optional<ScoreDto> scoreDto = scoreRepository.findOptionalById(scoreId).map(s -> new ScoreDto(s));

        return scoreDto.get();

    }

}















