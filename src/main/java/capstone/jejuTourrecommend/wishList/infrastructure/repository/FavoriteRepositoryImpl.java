package capstone.jejuTourrecommend.wishList.infrastructure.repository;

import capstone.jejuTourrecommend.authentication.domain.Member;
import capstone.jejuTourrecommend.wishList.domain.Favorite;
import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteListDto;
import capstone.jejuTourrecommend.wishList.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepository {

	private final FavoriteJpaRepository favoriteJpaRepository;
	private final FavoriteQuerydslRepository favoriteQuerydslRepository;

	@Override
	public Page<Favorite> findByMember(Member member, Pageable pageable) {
		return favoriteJpaRepository.findByMember(member, pageable);
	}

	@Override
	public Optional<Favorite> findOptionById(Long favoriteId) {
		return favoriteJpaRepository.findOptionById(favoriteId);
	}

	@Override
	public Optional<Favorite> findOptionByNameAndMemberId(String favoriteName, Long memberId) {
		return favoriteJpaRepository.findOptionByNameAndMemberId(favoriteName, memberId);
	}

	@Override
	public Optional<Favorite> findByNameAndMember(String favoriteName, Member member) {
		return favoriteJpaRepository.findByNameAndMember(favoriteName, member);
	}

	@Override
	public void deleteById(Long favoriteId) {
		favoriteJpaRepository.deleteById(favoriteId);
	}

	@Override
	public void save(Favorite favorite) {
		favoriteJpaRepository.save(favorite);
	}

	@Override
	public List<Long> findFavoriteIdListByMemberId(Long memberId) {
		return favoriteJpaRepository.findFavoriteIdListByMemberId(memberId);
	}

	@Override
	public Boolean isFavoriteSpot(Long memberId, Long spotId, List<Long> favoriteList) {
		return favoriteQuerydslRepository.isFavoriteSpot(memberId, spotId, favoriteList);
	}

	@Override
	public Page<FavoriteListDto> getFavoriteList(Long memberId, Pageable pageable) {
		return favoriteQuerydslRepository.getFavoriteList(memberId, pageable);
	}


}
