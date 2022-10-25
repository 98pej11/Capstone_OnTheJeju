package capstone.jejuTourrecommend.spot.domain.detailSpot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.PictureDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ReviewDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ScoreDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.SpotDetailDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.SpotDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.PictureRepository;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.ReviewRepository;
import capstone.jejuTourrecommend.spot.domain.mainSpot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DetailSpotQueryService implements DetailSpotQueryUseCase {

	private final SpotRepository spotRepository;
	private final ReviewRepository reviewRepository;
	private final PictureRepository pictureRepository;

	//readONly
	public Page<ReviewDto> reviewPage(Long spotId, Pageable pageable) {

		Spot spot = spotRepository.findOptionById(spotId)
			.orElseThrow(() -> new UserException("spotId가 올바르지 않습니다."));

		//리뷰 데이터 받아오기
		Page<ReviewDto> reviewDtoList = reviewRepository.searchSpotReview(spot, pageable);

		return reviewDtoList;

	}

	//readONly
	public SpotDetailDto spotPage(Long spotId, Long memberId) {

		Spot spot = spotRepository.findOptionById(spotId)
			.orElseThrow(() -> new UserException("spotId가 올바르지 않습니다."));
		SpotDto spotDto = new SpotDto(spot);

		List<PictureDto> pictureDtoList = pictureRepository.findBySpot(
				spot).stream().map(picture -> new PictureDto(picture))
			.collect(Collectors.toList());

		ScoreDto scoreDto = spotRepository.searchScore(spot);

		Boolean isFavoriteSpot = spotRepository.isFavoriteSpot(memberId, spotId);

		return new SpotDetailDto(spotDto, scoreDto, pictureDtoList, isFavoriteSpot);

	}

}














