package capstone.jejuTourrecommend.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1360750169L;

    public static final QMember member = new QMember("member1");

    public final capstone.jejuTourrecommend.domain.baseEntity.QBaseEntity _super = new capstone.jejuTourrecommend.domain.baseEntity.QBaseEntity(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final ListPath<Favorite, QFavorite> favorites = this.<Favorite, QFavorite>createList("favorites", Favorite.class, QFavorite.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<MemberSpot, QMemberSpot> memberSpotList = this.<MemberSpot, QMemberSpot>createList("memberSpotList", MemberSpot.class, QMemberSpot.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final StringPath refreshToken = createString("refreshToken");

    public final StringPath role = createString("role");

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

