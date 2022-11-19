package capstone.jejuTourrecommend.wishList.presentation.dto.response;

import capstone.jejuTourrecommend.wishList.domain.dto.SpotListDtoByFavoriteSpot;
import capstone.jejuTourrecommend.wishList.domain.service.request.FavoriteSpotSaveDto;
import capstone.jejuTourrecommend.wishList.domain.service.response.FavoriteSpotsDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteSpotsResponse {

	private Long status;
	private boolean success;
	private String message;
	private String favoriteName;
	private List<SpotListDtoByFavoriteSpot> spotListDtoByFavoriteSpots;

	public static FavoriteSpotsResponse from(Long status,boolean success,String message,FavoriteSpotsDto favoriteSpotsDto) {
		return new FavoriteSpotsResponse(status, success, message, favoriteSpotsDto.getFavoriteName(),
			favoriteSpotsDto.getSpotListDtoByFavoriteSpots());
	}
}





