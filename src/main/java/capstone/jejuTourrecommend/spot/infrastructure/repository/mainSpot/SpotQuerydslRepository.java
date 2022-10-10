package capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot;

import capstone.jejuTourrecommend.spot.domain.mainSpot.Category;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ScoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface SpotQuerydslRepository {

    ScoreDto searchScore(Spot spot);

    Boolean isFavoriteSpot(Long memberId, Long spotId);

    Page<SpotListDto> searchBySpotNameContains(Long memberId, String spotName, Pageable pageable);

    Page<SpotListDto> searchSpotByLocationAndCategory(Long memberId, List locationList, Category category, Pageable pageable);

    Page<SpotListDto> searchSpotByUserPriority(Long memberId, List locationList, UserWeightDto userWeightDto, Pageable pageable);

}
