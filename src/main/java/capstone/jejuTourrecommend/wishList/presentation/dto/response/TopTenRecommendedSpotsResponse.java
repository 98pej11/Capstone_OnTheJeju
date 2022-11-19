package capstone.jejuTourrecommend.wishList.presentation.dto.response;

import capstone.jejuTourrecommend.wishList.domain.dto.RouteSpotListDto;
import capstone.jejuTourrecommend.wishList.domain.service.response.TopTenRecommendedSpotsDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopTenRecommendedSpotsResponse {

	private Long status;
	private boolean success;
	private String message;
	private List<List<RouteSpotListDto>> spotList;

	public static TopTenRecommendedSpotsResponse from(Long status, boolean success, String message, TopTenRecommendedSpotsDto topTenRecommendedSpotsDto) {
		return new TopTenRecommendedSpotsResponse(status, success,message, topTenRecommendedSpotsDto.getSpotList());
	}
}










