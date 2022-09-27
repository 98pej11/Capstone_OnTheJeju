package capstone.jejuTourrecommendV2.repository;

import capstone.jejuTourrecommendV2.domain.Member;
import capstone.jejuTourrecommendV2.domain.MemberSpot;
import capstone.jejuTourrecommendV2.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberSpotRepository extends JpaRepository<MemberSpot,Long> {
     Optional<MemberSpot> findBySpotAndMember(Spot spot, Member member);

}





