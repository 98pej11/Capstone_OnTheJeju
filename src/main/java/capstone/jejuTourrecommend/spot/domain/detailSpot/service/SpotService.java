package capstone.jejuTourrecommend.spot.domain.detailSpot.service;


import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot.PictureJpaRepository;
import capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot.ReviewJpaRepository;
import capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot.ReviewQuerydslRepository;
import capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot.SpotJpaRepository;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.*;
import capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot.SpotQuerydslRepository;
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

    private final SpotJpaRepository spotJpaRepository;
    private final SpotQuerydslRepository spotQuerydslRepository;
    private final ReviewQuerydslRepository reviewQuerydslRepository;
    private final PictureJpaRepository pictureJpaRepository;


    public Page<ReviewDto> reviewPage(Long spotId, Pageable pageable){

        Spot spot = spotJpaRepository.findOptionById(spotId)
                .orElseThrow(() -> new UserException("spotId가 올바르지 않습니다."));

        //리뷰 데이터 받아오기
        Page<ReviewDto> reviewDtoList = reviewQuerydslRepository.searchSpotReview(spot, pageable);

        return reviewDtoList;

    }

    public SpotDetailDto spotPage(Long spotId, Long memberId){

        Spot spot = spotJpaRepository.findOptionById(spotId)
                .orElseThrow(() -> new UserException("spotId가 올바르지 않습니다."));
        SpotDto spotDto = new SpotDto(spot);



        List<PictureDto> pictureDtoList = pictureJpaRepository.findBySpot(
                        spot).stream().map(picture -> new PictureDto(picture))
                .collect(Collectors.toList());

        ScoreDto scoreDto = spotQuerydslRepository.searchScore(spot);

        Boolean isFavoriteSpot = spotQuerydslRepository.isFavoriteSpot(memberId, spotId);


        return new SpotDetailDto(spotDto,scoreDto,pictureDtoList, isFavoriteSpot);

    }






}














