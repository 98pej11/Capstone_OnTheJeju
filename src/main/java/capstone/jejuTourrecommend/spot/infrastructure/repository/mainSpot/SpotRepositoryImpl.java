package capstone.jejuTourrecommend.spot.infrastructure.repository.mainSpot;

import capstone.jejuTourrecommend.spot.domain.mainSpot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class SpotRepositoryImpl implements SpotRepository {

    private final SpotJpaQuerydslRepository spotJpaQuerydslRepository;

    private final SpotQuerydslRepository spotQuerydslRepository;






}
