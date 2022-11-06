package capstone.jejuTourrecommend.spot.infrastructure.repository.detailSpot;

import capstone.jejuTourrecommend.spot.domain.Spot;
import capstone.jejuTourrecommend.spot.domain.detailSpot.dto.ReviewDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static capstone.jejuTourrecommend.spot.domain.detailSpot.QReview.review;

@Repository
@Slf4j
@Transactional
public class ReviewQuerydslRepositoryImpl implements ReviewQuerydslRepository {

	private final JPAQueryFactory queryFactory;

	public ReviewQuerydslRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<ReviewDto> searchSpotReview(Spot spot, Pageable pageable) {
		//여기 spot을 불어오지 않고 id로 받을수 있을 것 같음
		List<ReviewDto> contents = queryFactory
			.select(Projections.constructor(ReviewDto.class,
					review.id,//여기서 as()안써도 됨 어차지 필드가 아니라 생서자를 통해서 한거라
					review.content
				)
			)
			.from(review)
			.where(review.spot.eq(spot))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(review.count())
			.from(review)
			.where(review.spot.eq(spot));

		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}
}















