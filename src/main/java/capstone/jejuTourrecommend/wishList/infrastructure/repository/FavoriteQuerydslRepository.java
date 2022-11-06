package capstone.jejuTourrecommend.wishList.infrastructure.repository;

public interface FavoriteQuerydslRepository {

	Boolean isFavoriteSpot(Long memberId, Long spotId);

}
