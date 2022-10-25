package capstone.jejuTourrecommend.wishList.presentation.dto.response;

import java.util.List;

import capstone.jejuTourrecommend.wishList.domain.dto.SpotListDtoByFavoriteSpot;
import lombok.Data;

@Data
public class ResultFavoriteSpotList {

	private Long status;
	private boolean success;
	private String message;
	private String favoriteName;
	private List<SpotListDtoByFavoriteSpot> spotListDtoByFavoriteSpots;

	public ResultFavoriteSpotList(Long status, boolean success, String message, String favoriteName,
		List<SpotListDtoByFavoriteSpot> spotListDtoByFavoriteSpots) {
		this.status = status;
		this.success = success;
		this.message = message;
		this.favoriteName = favoriteName;
		this.spotListDtoByFavoriteSpots = spotListDtoByFavoriteSpots;
	}
}





