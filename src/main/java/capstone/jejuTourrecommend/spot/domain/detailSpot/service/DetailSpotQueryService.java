package capstone.jejuTourrecommend.spot.domain.detailSpot.service;

import capstone.jejuTourrecommend.common.exceptionClass.UserException;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.*;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.PictureRepository;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.ReviewRepository;
import capstone.jejuTourrecommend.spot.domain.detailSpot.repository.ScoreRepository;
import capstone.jejuTourrecommend.spot.domain.mainSpot.repository.SpotRepository;
import capstone.jejuTourrecommend.wishList.domain.repository.FavoriteRepository;
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
@Transactional(readOnly = true)
public class DetailSpotQueryService implements DetailSpotQueryUseCase {

	private final SpotRepository spotRepository;
	private final ReviewRepository reviewRepository;
	private final PictureRepository pictureRepository;
	private final ScoreRepository scoreRepository;
	private final FavoriteRepository favoriteRepository;

	//readONly
	public Page<ReviewDto> reviewPage(Long spotId, Pageable pageable) {
		Spot spot = spotRepository.findOptionById(spotId)
			.orElseThrow(() -> new UserException("spotId가 올바르지 않습니다."));
		Page<ReviewDto> reviewDtoList = reviewRepository.searchSpotReview(spot, pageable);
		return reviewDtoList;

	}

	//readONly
	public SpotDetailDto spotPage(Long spotId, Long memberId) {
		SpotDto spotDto = spotRepository.findOptionById(spotId).map(s -> new SpotDto(s))
			.orElseThrow(() -> new UserException("spotId가 올바르지 않습니다."));
		List<PictureDto> pictureDtoList = pictureRepository.findBySpotId(spotId).stream()
			.map(picture -> new PictureDto(picture))
			.collect(Collectors.toList());
		ScoreDto scoreDto = scoreRepository.findScoreBySpotId(spotId);
		List<Long> favoriteIdList = favoriteRepository.findFavoriteIdListByMemberId(memberId);
		Boolean isFavoriteSpot = favoriteRepository.isFavoriteSpot(memberId, spotId, favoriteIdList);
		return new SpotDetailDto(spotDto, scoreDto, pictureDtoList, isFavoriteSpot);
	}

}














