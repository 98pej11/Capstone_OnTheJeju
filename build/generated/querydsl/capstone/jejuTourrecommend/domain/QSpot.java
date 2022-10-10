package capstone.jejuTourrecommend.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSpot is a Querydsl query type for Spot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpot extends EntityPathBase<Spot> {

    private static final long serialVersionUID = -1455376255L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSpot spot = new QSpot("spot");

    public final StringPath address = createString("address");

    public final StringPath description = createString("description");

    public final ListPath<FavoriteSpot, QFavoriteSpot> favoriteSpotList = this.<FavoriteSpot, QFavoriteSpot>createList("favoriteSpotList", FavoriteSpot.class, QFavoriteSpot.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<Location> location = createEnum("location", Location.class);

    public final ListPath<MemberSpot, QMemberSpot> memberSpotList = this.<MemberSpot, QMemberSpot>createList("memberSpotList", MemberSpot.class, QMemberSpot.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final ListPath<Picture, QPicture> pictures = this.<Picture, QPicture>createList("pictures", Picture.class, QPicture.class, PathInits.DIRECT2);

    public final ListPath<Review, QReview> reviews = this.<Review, QReview>createList("reviews", Review.class, QReview.class, PathInits.DIRECT2);

    public final QScore score;

    public QSpot(String variable) {
        this(Spot.class, forVariable(variable), INITS);
    }

    public QSpot(Path<? extends Spot> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSpot(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSpot(PathMetadata metadata, PathInits inits) {
        this(Spot.class, metadata, inits);
    }

    public QSpot(Class<? extends Spot> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.score = inits.isInitialized("score") ? new QScore(forProperty("score"), inits.get("score")) : null;
    }

}

