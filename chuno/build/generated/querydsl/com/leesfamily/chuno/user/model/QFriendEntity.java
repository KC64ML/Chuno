package com.leesfamily.chuno.user.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFriendEntity is a Querydsl query type for FriendEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFriendEntity extends EntityPathBase<FriendEntity> {

    private static final long serialVersionUID = -246584682L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFriendEntity friendEntity = new QFriendEntity("friendEntity");

    public final com.leesfamily.chuno.common.model.QBaseTimeEntity _super = new com.leesfamily.chuno.common.model.QBaseTimeEntity(this);

    public final QUserEntity fromUser;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDt = _super.modDt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    public final QUserEntity toUser;

    public QFriendEntity(String variable) {
        this(FriendEntity.class, forVariable(variable), INITS);
    }

    public QFriendEntity(Path<? extends FriendEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFriendEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFriendEntity(PathMetadata metadata, PathInits inits) {
        this(FriendEntity.class, metadata, inits);
    }

    public QFriendEntity(Class<? extends FriendEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fromUser = inits.isInitialized("fromUser") ? new QUserEntity(forProperty("fromUser"), inits.get("fromUser")) : null;
        this.toUser = inits.isInitialized("toUser") ? new QUserEntity(forProperty("toUser"), inits.get("toUser")) : null;
    }

}

