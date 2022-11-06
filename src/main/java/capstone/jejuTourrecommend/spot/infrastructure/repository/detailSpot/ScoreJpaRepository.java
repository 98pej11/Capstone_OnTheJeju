package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.Score;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ScoreDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScoreJpaRepository extends JpaRepository<Score, Long> {

	@Query(value = "select " +
		"new capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ScoreDto(score.id, score.viewScore, score.priceScore, score.facilityScore, score.priceScore, score.viewRank, score.priceRank, score.facilityRank, score.priceRank) " +
		"from Score score " +
		"where score.spot.id = :spotId")
	ScoreDto findScoreBySpotId(@Param("spotId") Long spotId);

}
