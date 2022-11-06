package capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot;

import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberSpotQuerydslRepository {

	void updateMemberSpotByPriority(Long memberId, UserWeightDto userWeightDto);

	Page<SpotListDto> searchSpotByUserPriority(Long memberId, List locationList, Pageable pageable);

}
