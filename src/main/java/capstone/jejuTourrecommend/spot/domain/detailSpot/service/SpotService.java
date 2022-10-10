package capstone.jejuTourrecommend.spot.domain.detailSpot.service;


import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.domain.Spot;
import capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot.PictureJpaRepository;
import capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot.ReviewJpaQuerydslRepository;
import capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot.SpotJpaQuerydslRepository;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SpotService {

    private final SpotJpaQuerydslRepository spotJpaRepository;
    private final ReviewJpaQuerydslRepository reviewJpaRepository;
    private final PictureJpaRepository pictureJpaRepository;


    public Page<ReviewDto> reviewPage(Long spotId, Pageable pageable){

        Spot spot = spotJpaRepository.findOptionById(spotId)
                .orElseThrow(() -> new UserException("spotId가 올바르지 않습니다."));

        //리뷰 데이터 받아오기
        Page<ReviewDto> reviewDtoList = reviewJpaRepository.searchSpotReview(spot, pageable);

        return reviewDtoList;

    }

    public SpotDetailDto spotPage(Long spotId, Long memberId){

        Spot spot = spotJpaRepository.findOptionById(spotId)
                .orElseThrow(() -> new UserException("spotId가 올바르지 않습니다."));
        SpotDto spotDto = new SpotDto(spot);



        List<PictureDto> pictureDtoList = pictureJpaRepository.findBySpot(
                        spot).stream().map(picture -> new PictureDto(picture))
                .collect(Collectors.toList());

        ScoreDto scoreDto = spotJpaRepository.searchScore(spot);

        Boolean isFavoriteSpot = spotJpaRepository.isFavoriteSpot(memberId, spotId);


        return new SpotDetailDto(spotDto,scoreDto,pictureDtoList, isFavoriteSpot);

    }






}














