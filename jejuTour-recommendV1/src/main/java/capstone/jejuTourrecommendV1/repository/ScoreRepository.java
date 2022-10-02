package capstone.jejuTourrecommendV1.repository;

import capstone.jejuTourrecommendV1.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score,Long> {


    Optional<Score> findOptionalById(Long scoreId);
}
