package capstone.jejuTourrecommend.spot.presentation.response;

import org.springframework.data.domain.Page;

import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ReviewDto;
import lombok.Data;

@Data
public class ReviewListDto {

	private Long status;
	private boolean success;
	private String message;

	private Page<ReviewDto> reviewListDto;

	public ReviewListDto(Long status, boolean success, String message, Page<ReviewDto> reviewListDto) {
		this.status = status;
		this.success = success;
		this.message = message;
		this.reviewListDto = reviewListDto;
	}
}
