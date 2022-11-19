package capstone.jejuTourrecommend.spot.presentation.response;

import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ReviewDto;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class ReviewsResponse {

	private Long status;
	private boolean success;
	private String message;

	private Page<ReviewDto> reviewListDto;

	public ReviewsResponse(Long status, boolean success, String message, Page<ReviewDto> reviewListDto) {
		this.status = status;
		this.success = success;
		this.message = message;
		this.reviewListDto = reviewListDto;
	}
}
