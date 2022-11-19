package capstone.jejuTourrecommend.wishList.domain.service.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteSpotSaveDto {

	private Long spotId;
	private Long favoriteId;

}


