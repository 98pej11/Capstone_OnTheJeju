package capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot;

import capstone.jejuTourrecommend.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpotJpaQuerydslRepository extends JpaRepository<Spot, Long> , SpotQuerydslRepository {

    Optional<Spot> findOptionByName(String spotName);

    //Optional<Spot> findOptionBySpot(Spot spot);

    Optional<Spot> findOptionById(Long spotId);


    List<Spot> findByNameLike(String spotName);


}