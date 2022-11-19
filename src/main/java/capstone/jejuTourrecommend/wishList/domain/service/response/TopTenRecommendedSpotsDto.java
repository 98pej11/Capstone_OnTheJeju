package capstone.jejuTourrecommend.wishList.presentation.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class TopTenRecommendedSpotsResponse {

	private Long status;
	private boolean success;
	private String message;
	private List spotList;

	public TopTenRecommendedSpotsResponse(Long status, boolean success, String message, List spotList) {
		this.status = status;
		this.success = success;
		this.message = message;
		this.spotList = spotList;
	}
}










