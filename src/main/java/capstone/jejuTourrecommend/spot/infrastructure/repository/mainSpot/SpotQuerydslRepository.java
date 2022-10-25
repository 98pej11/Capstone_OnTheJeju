package capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ScoreDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Category;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;

public interface SpotQuerydslRepository {

	ScoreDto searchScore(Spot spot);

	Boolean isFavoriteSpot(Long memberId, Long spotId);

	Page<SpotListDto> searchBySpotNameContains(Long memberId, String spotName, Pageable pageable);

	Page<SpotListDto> searchSpotByLocationAndCategory(Long memberId, List locationList, Category category,
		Pageable pageable);

	Page<SpotListDto> searchSpotByUserPriority(Long memberId, List locationList, UserWeightDto userWeightDto,
		Pageable pageable);

}
