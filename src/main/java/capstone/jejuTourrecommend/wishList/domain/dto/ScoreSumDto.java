package capstone.jejuTourrecommend.wishList.domain.dto;

import lombok.Data;

@Data
public class ScoreSumDto {

	private Double viewScoreSum;
	private Double priceScoreSum;
	private Double facilityScoreSum;
	private Double surroundScoreSum;

	public ScoreSumDto(Double viewScoreSum, Double priceScoreSum, Double facilityScoreSum, Double surroundScoreSum) {
		this.viewScoreSum = viewScoreSum;
		this.priceScoreSum = priceScoreSum;
		this.facilityScoreSum = facilityScoreSum;
		this.surroundScoreSum = surroundScoreSum;
	}
}
