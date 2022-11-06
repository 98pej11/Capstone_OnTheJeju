package capstone.jejuTourrecommend.spot.domain.detailSpot.dto;

import lombok.Data;

@Data
public class ScoreDto {

	private Long id;

	private Double viewScore;
	private Double priceScore;
	private Double facilityScore;
	private Double surroundScore;

	private Double viewRank;
	private Double priceRank;
	private Double facilityRank;
	private Double surroundRank;

	public ScoreDto(Long id, Double viewScore, Double priceScore,
		Double facilityScore, Double surroundScore,
		Double viewRank, Double priceRank,
		Double facilityRank, Double surroundRank) {
		this.id = id;
		this.viewScore = viewScore;
		this.priceScore = priceScore;
		this.facilityScore = facilityScore;
		this.surroundScore = surroundScore;
		this.viewRank = viewRank;
		this.priceRank = priceRank;
		this.facilityRank = facilityRank;
		this.surroundRank = surroundRank;
	}

}
