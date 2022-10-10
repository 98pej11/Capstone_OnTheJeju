package capstone.jejuTourrecommend.spot.domain.mainSpot.repository;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.spot.domain.mainSpot.MemberSpot;
import capstone.jejuTourrecommend.spot.domain.Spot;

import java.util.Optional;

public interface MemberSpotRepository{

    Optional<MemberSpot> findBySpotAndMember(Spot spot, Member member);

    int bulkDeleteMemberSpotByMember(Member member);
}
