package capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.mainSpot.MemberSpot;
import capstone.jejuTourrecommend.spot.domain.mainSpot.repository.MemberSpotRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberSpotRepositoryImpl implements MemberSpotRepository {

	private final MemberSpotJpaRepository memberSpotJpaRepository;

	@Override
	public Optional<MemberSpot> findBySpotAndMember(Spot spot, Member member) {
		return memberSpotJpaRepository.findBySpotAndMember(spot, member);
	}

	@Override
	public int bulkDeleteMemberSpotByMember(Member member) {
		return memberSpotJpaRepository.bulkDeleteMemberSpotByMember(member);
	}
}