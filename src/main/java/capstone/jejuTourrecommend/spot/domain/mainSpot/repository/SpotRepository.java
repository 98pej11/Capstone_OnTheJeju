package capstone.jejuTourrecommend.spot.domain.mainSpot.repository;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Category;
import capstone.jejuTourrecommend.spot.domain.mainSpot.Location;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.wishList.domain.dto.RouteSpotListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SpotRepository {

	Optional<Spot> findOptionByName(String spotName);

	Optional<Spot> findOptionById(Long spotId);

	List<Spot> findByNameLike(String spotName);

	Page<SpotListDto> searchBySpotNameContains(Long memberId, String spotName, Pageable pageable);

	Page<SpotListDto> searchSpotByLocationAndCategory(Long memberId, List locationList, Category category,
		Pageable pageable);

	List<Spot> findSpotFetchJoinBySpotIdList(List<Long> spotIdList);

	List<Location> findDistinctLocationBySpotIdList(List<Long> spotIdList);

	List<RouteSpotListDto> getRouteSpotListDtos(Location location, Category category);

}
