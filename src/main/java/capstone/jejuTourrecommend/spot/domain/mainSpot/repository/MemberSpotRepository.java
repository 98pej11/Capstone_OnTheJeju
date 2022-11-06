package capstone.jejuTourrecommend.spot.domain.mainSpot.repository;

import java.util.List;
import java.util.Optional;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.mainSpot.MemberSpot;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberSpotRepository {

	Optional<MemberSpot> findBySpotAndMember(Spot spot, Member member);

	int bulkDeleteMemberSpotByMember(Member member);

	void updateMemberSpotByPriority(Long memberId, UserWeightDto userWeightDto);

	Page<SpotListDto> searchSpotByUserPriority(Long memberId, List locationList, Pageable pageable);
}
