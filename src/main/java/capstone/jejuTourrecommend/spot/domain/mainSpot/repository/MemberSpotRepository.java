package capstone.jejuTourrecommend.spot.domain.mainSpot.repository;

import java.util.Optional;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.mainSpot.MemberSpot;

public interface MemberSpotRepository {

	Optional<MemberSpot> findBySpotAndMember(Spot spot, Member member);

	int bulkDeleteMemberSpotByMember(Member member);
}
