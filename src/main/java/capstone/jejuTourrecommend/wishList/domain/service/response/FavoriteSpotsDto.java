package capstone.jejuTourrecommend.wishList.domain.service.response;

import capstone.jejuTourrecommend.wishList.domain.dto.SpotListDtoByFavoriteSpot;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteSpotsDto {

	private String favoriteName;
	private List<SpotListDtoByFavoriteSpot> spotListDtoByFavoriteSpots;

	public static FavoriteSpotsDto from(String favoriteName, List<SpotListDtoByFavoriteSpot> spotListDtoByFavoriteSpots) {
		return FavoriteSpotsDto.builder()
			.favoriteName(favoriteName)
			.spotListDtoByFavoriteSpots(spotListDtoByFavoriteSpots)
			.build();
	}

}





