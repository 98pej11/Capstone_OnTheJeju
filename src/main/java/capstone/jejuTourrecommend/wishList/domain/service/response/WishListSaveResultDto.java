package capstone.jejuTourrecommend.wishList.domain.service.response;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteDto;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WishListSaveDto {

	private Long status;
	private boolean success;
	private String message;
	private FavoriteDto favoriteDto;


}
