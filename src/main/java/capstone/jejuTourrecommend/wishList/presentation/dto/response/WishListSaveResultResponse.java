package capstone.jejuTourrecommend.wishList.presentation.dto.response;

import capstone.jejuTourrecommend.wishList.domain.dto.FavoriteDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishListSaveResultResponse {

	private Long status;
	private boolean success;
	private String message;
	private FavoriteDto favoriteDto;

	public static WishListSaveResultResponse from(Long status, boolean success, String message,FavoriteDto favoriteDto) {
		return new WishListSaveResultResponse(status,
			success, message, favoriteDto);

	}
}
