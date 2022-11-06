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
		List<ReviewDto> contents = queryFactory
			.select(Projections.constructor(ReviewDto.class,
					review.id,
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















