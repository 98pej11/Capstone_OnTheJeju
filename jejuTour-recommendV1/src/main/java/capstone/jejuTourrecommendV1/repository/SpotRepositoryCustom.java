package capstone.jejuTourrecommendV1.repository;

import capstone.jejuTourrecommendV1.domain.Category;
import capstone.jejuTourrecommendV1.domain.Spot;
import capstone.jejuTourrecommendV1.web.pageDto.mainPage.OptimizationSpotListDto;
import capstone.jejuTourrecommendV1.web.pageDto.mainPage.SpotListDto;
import capstone.jejuTourrecommendV1.web.pageDto.mainPage.UserWeightDto;
import capstone.jejuTourrecommendV1.web.pageDto.spotPage.ScoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface SpotRepositoryCustom {

    Page<SpotListDto> searchSpotByLocationAndCategory(List locationList, Category category, Pageable pageable);

    Page<SpotListDto> searchSpotByUserPriority(Long memberId, List locationList, UserWeightDto userWeightDto, Pageable pageable);

    //Page<SpotDetailDto> searchSpotDetail(String spotName);

    ScoreDto searchScore(Spot spot);

    Boolean isFavoriteSpot(Long memberId, Long spotId);

    Page<SpotListDto> searchBySpotNameContains(String spotName, Pageable pageable);


    Page<OptimizationSpotListDto> optimizationSearchSpotByLocationAndCategory(Long memberId, List locationList, Category category, Pageable pageable);

    Page<OptimizationSpotListDto> optimizationSearchSpotByUserPriority(Long memberId, List locationList, UserWeightDto userWeightDto, Pageable pageable);

}
