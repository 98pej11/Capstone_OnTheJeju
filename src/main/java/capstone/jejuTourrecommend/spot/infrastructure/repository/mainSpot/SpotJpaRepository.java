package capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import capstone.jejuTourrecommend.spot.domain.Spot;

public interface SpotJpaRepository extends JpaRepository<Spot, Long> {

	Optional<Spot> findOptionByName(String spotName);

	Optional<Spot> findOptionById(Long spotId);

	List<Spot> findByNameLike(String spotName);

}

