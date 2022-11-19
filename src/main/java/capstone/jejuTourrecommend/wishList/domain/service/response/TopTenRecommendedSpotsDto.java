package capstone.jejuTourrecommend.wishList.domain.service.response;

import capstone.jejuTourrecommend.wishList.domain.dto.RouteSpotListDto;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TopTenRecommendedSpotsDto {

	private List<List<RouteSpotListDto>> spotList;

	public static TopTenRecommendedSpotsDto from(List<List<RouteSpotListDto>> spotList) {
		return TopTenRecommendedSpotsDto.builder()
			.spotList(spotList)
			.build();
	}

}










