package capstone.jejuTourrecommend.spot.domain.mainSpot.dto;

import lombok.Data;

@Data
public class FavoriteSpotDto {

	private Long spotId;

	private Long favoriteId;

	public FavoriteSpotDto(Long spotId, Long favoriteId) {
		this.spotId = spotId;
		this.favoriteId = favoriteId;
	}
}
