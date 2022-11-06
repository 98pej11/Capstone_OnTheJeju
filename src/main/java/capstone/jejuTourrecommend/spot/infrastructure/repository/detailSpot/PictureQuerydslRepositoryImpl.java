package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.PictureDetailDto;
import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.SpotListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static capstone.jejuTourrecommend.spot.domain.QSpot.spot;
import static capstone.jejuTourrecommend.spot.domain.detailSpot.QPicture.picture;

@Repository
@Slf4j
@Transactional
public class PictureQuerydslRepositoryImpl implements PictureQuerydslRepository{

	private final JPAQueryFactory queryFactory;

	public PictureQuerydslRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	public List<PictureDetailDto> postSpotPictureUrlsToDto(List<SpotListDto> spotListDtoList) {

		List<Long> spotIdList = spotListDtoList.stream().map(o -> o.getSpotId()).collect(Collectors.toList());

		//to many 관계를 한번에 many 기준으로 한번에 가져오고 map을 이용하여 O(1)시간으로 단축 시켰다
		List<PictureDetailDto> pictureDetailDtoList = queryFactory
			.select(Projections.constructor(PictureDetailDto.class,
					picture.id,
					picture.url,
					picture.spot.id
				)
			)
			.from(picture)
			.innerJoin(picture.spot, spot)
			.where(picture.spot.id.in(spotIdList))
			.fetch();

		return pictureDetailDtoList;

	}



}
