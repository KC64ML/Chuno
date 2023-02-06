package com.leesfamily.chuno.room.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPushEntity is a Querydsl query type for PushEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPushEntity extends EntityPathBase<PushEntity> {

    private static final long serialVersionUID = 196675650L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPushEntity pushEntity = new QPushEntity("pushEntity");

    public final com.leesfamily.chuno.common.model.QBaseTimeEntity _super = new com.leesfamily.chuno.common.model.QBaseTimeEntity(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDt = _super.modDt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    public final QRoomEntity room;

    public final com.leesfamily.chuno.user.model.QUserEntity user;

    public QPushEntity(String variable) {
        this(PushEntity.class, forVariable(variable), INITS);
    }

    public QPushEntity(Path<? extends PushEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPushEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPushEntity(PathMetadata metadata, PathInits inits) {
        this(PushEntity.class, metadata, inits);
    }

    public QPushEntity(Class<? extends PushEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.room = inits.isInitialized("room") ? new QRoomEntity(forProperty("room"), inits.get("room")) : null;
        this.user = inits.isInitialized("user") ? new com.leesfamily.chuno.user.model.QUserEntity(forProperty("user"), inits.get("user")) : null;
    }

}

