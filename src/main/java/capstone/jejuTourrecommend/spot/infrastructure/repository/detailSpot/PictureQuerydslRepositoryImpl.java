package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.mainSpot.dto.PictureDetailDto;
import capstone.jejuTourrecommend.wishList.domain.dto.PictureUrlDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

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

	@Override
	public List<PictureDetailDto> getPictureDetailDtoBySpotIdList(List<Long> spotIdList) {
		//to many 관계를 한번에 many 기준으로 한번에 가져오고 map 을 이용하여 O(1)시간으로 단축
		return queryFactory
			.select(Projections.constructor(PictureDetailDto.class,
					picture.id,
					picture.url,
					picture.spot.id
				)
			)
			.from(picture)
			.where(picture.spot.id.in(spotIdList))
			.fetch();
	}

	@Override
	public List<PictureUrlDto> findPictureUrlDtos(List<Long> spotIdList, Integer limit){
		//querydsl groupBy 는 정렬 조건을 주어지지 않으면 fileSort 로 정렬후 groupBy 진행, 그러나 picture 테이블을 spotId 로 인덱싱 해주어서 orderByNull 불필요
		return queryFactory
			.select(Projections.constructor(PictureUrlDto.class,
					picture.spot.id,
					picture.url.max()
				)
			)
			.from(picture)
			.where(picture.spot.id.in(spotIdList))
			.groupBy(picture.spot.id)
			.limit(limit) // "limit 개의 spot"의 picture 1개 가져오기
			.fetch();
	}



}
