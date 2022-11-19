package capstone.jejuTourrecommend.wishList.presentation.dto.response;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteDto;
import capstone.jejuTourrecommend.wishList.domain.service.request.WishListSaveDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishListSaveResponse {

	private Long status;
	private boolean success;
	private String message;
	private FavoriteDto favoriteDto;

	public static WishListSaveResponse from(WishListSaveDto wishListSaveDto) {
		return new WishListSaveResponse(wishListSaveDto.g)

	}
}
