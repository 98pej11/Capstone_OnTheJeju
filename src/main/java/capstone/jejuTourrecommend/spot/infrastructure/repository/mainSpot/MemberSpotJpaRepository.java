package capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.spot.domain.mainSpot.MemberSpot;
import capstone.jejuTourrecommend.spot.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberSpotJpaRepository extends JpaRepository<MemberSpot,Long> {
     Optional<MemberSpot> findBySpotAndMember(Spot spot, Member member);


     @Modifying(clearAutomatically = true)
     @Query("delete from MemberSpot ms where ms.member = :member")
     int bulkDeleteMemberSpotByMember(@Param("member") Member member);


}





