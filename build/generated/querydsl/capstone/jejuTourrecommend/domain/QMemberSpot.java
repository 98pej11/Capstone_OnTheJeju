package capstone.jejuTourrecommend.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberSpot is a Querydsl query type for MemberSpot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberSpot extends EntityPathBase<MemberSpot> {

    private static final long serialVersionUID = -301596933L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberSpot memberSpot = new QMemberSpot("memberSpot");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final NumberPath<Double> score = createNumber("score", Double.class);

    public final QSpot spot;

    public QMemberSpot(String variable) {
        this(MemberSpot.class, forVariable(variable), INITS);
    }

    public QMemberSpot(Path<? extends MemberSpot> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberSpot(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberSpot(PathMetadata metadata, PathInits inits) {
        this(MemberSpot.class, metadata, inits);
    }

    public QMemberSpot(Class<? extends MemberSpot> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.spot = inits.isInitialized("spot") ? new QSpot(forProperty("spot"), inits.get("spot")) : null;
    }

}

