package capstone.jejuTourrecommend.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFavoriteSpot is a Querydsl query type for FavoriteSpot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFavoriteSpot extends EntityPathBase<FavoriteSpot> {

    private static final long serialVersionUID = -1155409603L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFavoriteSpot favoriteSpot = new QFavoriteSpot("favoriteSpot");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final QFavorite favorite;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QSpot spot;

    public QFavoriteSpot(String variable) {
        this(FavoriteSpot.class, forVariable(variable), INITS);
    }

    public QFavoriteSpot(Path<? extends FavoriteSpot> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFavoriteSpot(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFavoriteSpot(PathMetadata metadata, PathInits inits) {
        this(FavoriteSpot.class, metadata, inits);
    }

    public QFavoriteSpot(Class<? extends FavoriteSpot> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.favorite = inits.isInitialized("favorite") ? new QFavorite(forProperty("favorite"), inits.get("favorite")) : null;
        this.spot = inits.isInitialized("spot") ? new QSpot(forProperty("spot"), inits.get("spot")) : null;
    }

}

