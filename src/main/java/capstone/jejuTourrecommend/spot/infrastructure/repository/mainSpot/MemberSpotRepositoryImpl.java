package capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.mainSpot.MemberSpot;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.UserWeightDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.repository.MemberSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberSpotRepositoryImpl implements MemberSpotRepository {

	private final MemberSpotJpaRepository memberSpotJpaRepository;
	private final MemberSpotQuerydslRepository memberSpotQuerydslRepository;

	@Override
	public Optional<MemberSpot> findBySpotAndMember(Spot spot, Member member) {
		return memberSpotJpaRepository.findBySpotAndMember(spot, member);
	}

	@Override
	public int bulkDeleteMemberSpotByMember(Member member) {
		return memberSpotJpaRepository.bulkDeleteMemberSpotByMember(member);
	}

	@Override
	public void updateMemberSpotByPriority(Long memberId, UserWeightDto userWeightDto) {
		memberSpotQuerydslRepository.updateMemberSpotByPriority(memberId, userWeightDto);
	}

	@Override
	public Page<SpotListDto> searchSpotByUserPriority(Long memberId, List locationList, Pageable pageable) {
		return memberSpotQuerydslRepository.searchSpotByUserPriority(memberId, locationList, pageable);
	}
}
