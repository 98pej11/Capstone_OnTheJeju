package capstone.jejuTourrecommend.spot.presentation.response;

import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class SpotListResponse {

	private Long status;
	private boolean success;
	private String message;
	private Page<SpotListDto> data;

	public SpotListResponse(Long status, boolean success, String message, Page<SpotListDto> data) {
		this.status = status;
		this.success = success;
		this.message = message;
		this.data = data;
	}
}
