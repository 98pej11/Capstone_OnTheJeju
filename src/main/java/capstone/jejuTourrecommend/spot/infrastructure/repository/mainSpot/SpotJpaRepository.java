package capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot;

import capstone.jejuTourrecommend.spot.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpotJpaRepository extends JpaRepository<Spot, Long> {

    Optional<Spot> findOptionByName(String spotName);

    Optional<Spot> findOptionById(Long spotId);

    List<Spot> findByNameLike(String spotName);


}

