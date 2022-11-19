package capstone.jejuTourrecommend.wishList.domain.service.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WishListSaveDto {

	private Long spotId;
	private String favoriteName;
	private Long memberId;

}
