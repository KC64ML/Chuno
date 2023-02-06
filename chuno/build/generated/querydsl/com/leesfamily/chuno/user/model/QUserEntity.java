package com.leesfamily.chuno.user.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = -1647243453L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final com.leesfamily.chuno.common.model.QBaseTimeEntity _super = new com.leesfamily.chuno.common.model.QBaseTimeEntity(this);

    public final NumberPath<Integer> chaserPlayCount = createNumber("chaserPlayCount", Integer.class);

    public final NumberPath<Integer> chaserWinCount = createNumber("chaserWinCount", Integer.class);

    public final StringPath email = createString("email");

    public final NumberPath<Integer> exp = createNumber("exp", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.leesfamily.chuno.item.model.ItemEntity, com.leesfamily.chuno.item.model.QItemEntity> inventory = this.<com.leesfamily.chuno.item.model.ItemEntity, com.leesfamily.chuno.item.model.QItemEntity>createList("inventory", com.leesfamily.chuno.item.model.ItemEntity.class, com.leesfamily.chuno.item.model.QItemEntity.class, PathInits.DIRECT2);

    public final BooleanPath isManager = createBoolean("isManager");

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDt = _super.modDt;

    public final NumberPath<Integer> money = createNumber("money", Integer.class);

    public final StringPath nickname = createString("nickname");

    public final NumberPath<Integer> paperCount = createNumber("paperCount", Integer.class);

    public final QUserProfile profile;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    public final NumberPath<Integer> runnerPlayCount = createNumber("runnerPlayCount", Integer.class);

    public final NumberPath<Integer> runnerWinCount = createNumber("runnerWinCount", Integer.class);

    public QUserEntity(String variable) {
        this(UserEntity.class, forVariable(variable), INITS);
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserEntity(PathMetadata metadata, PathInits inits) {
        this(UserEntity.class, metadata, inits);
    }

    public QUserEntity(Class<? extends UserEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.profile = inits.isInitialized("profile") ? new QUserProfile(forProperty("profile")) : null;
    }

}

