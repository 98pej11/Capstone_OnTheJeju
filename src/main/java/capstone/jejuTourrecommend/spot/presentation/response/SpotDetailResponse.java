package capstone.jejuTourrecommend.spot.presentation.response;

import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.SpotDetailDto;
import lombok.Data;

@Data
public class SpotDetailResponse {

	private Long status;
	private boolean success;
	private String message;

	private SpotDetailDto data;

	public SpotDetailResponse(Long status, boolean success, String message, SpotDetailDto data) {
		this.status = status;
		this.success = success;
		this.message = message;
		this.data = data;
	}
}
