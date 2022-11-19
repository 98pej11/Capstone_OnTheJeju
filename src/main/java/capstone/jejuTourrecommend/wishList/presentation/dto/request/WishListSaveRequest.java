package capstone.jejuTourrecommend.wishList.presentation.dto.request;

import capstone.jejuTourrecommend.wishList.domain.service.request.WishListSaveDto;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WishListSaveRequest {

	private Long spotId;
	private String favoriteName;

	public WishListSaveDto toWishListSaveDto(Long memberId) {
		return WishListSaveDto.builder()
			.spotId(spotId)
			.favoriteName(favoriteName)
			.memberId(memberId)
			.build();
	}

}
