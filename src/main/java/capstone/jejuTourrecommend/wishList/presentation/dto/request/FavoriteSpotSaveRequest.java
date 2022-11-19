package capstone.jejuTourrecommend.wishList.presentation.dto.request;

import capstone.jejuTourrecommend.wishList.domain.service.request.FavoriteSpotSaveDto;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteSpotSaveRequest {

	private Long spotId;
	private Long favoriteId;

	public FavoriteSpotSaveDto toFavoriteSpotSaveDto(){
		return FavoriteSpotSaveDto.builder()
			.spotId(spotId)
			.favoriteId(favoriteId)
			.build();
	}

}


