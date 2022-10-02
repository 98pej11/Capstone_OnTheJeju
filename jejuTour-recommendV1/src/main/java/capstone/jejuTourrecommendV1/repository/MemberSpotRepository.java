package capstone.jejuTourrecommendV1.repository;

import capstone.jejuTourrecommendV1.domain.Member;
import capstone.jejuTourrecommendV1.domain.MemberSpot;
import capstone.jejuTourrecommendV1.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberSpotRepository extends JpaRepository<MemberSpot,Long> {
     Optional<MemberSpot> findBySpotAndMember(Spot spot, Member member);

}





