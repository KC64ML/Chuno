package com.leesfamily.chuno.room.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoomEntity is a Querydsl query type for RoomEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoomEntity extends EntityPathBase<RoomEntity> {

    private static final long serialVersionUID = -475282109L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoomEntity roomEntity = new QRoomEntity("roomEntity");

    public final NumberPath<Integer> currentPlayers = createNumber("currentPlayers", Integer.class);

    public final QDateTime dateTime;

    public final com.leesfamily.chuno.user.model.QUserEntity host;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isPublic = createBoolean("isPublic");

    public final NumberPath<Double> lat = createNumber("lat", Double.class);

    public final NumberPath<Double> lng = createNumber("lng", Double.class);

    public final NumberPath<Integer> maxPlayers = createNumber("maxPlayers", Integer.class);

    public final StringPath password = createString("password");

    public final NumberPath<Integer> radius = createNumber("radius", Integer.class);

    public final StringPath title = createString("title");

    public QRoomEntity(String variable) {
        this(RoomEntity.class, forVariable(variable), INITS);
    }

    public QRoomEntity(Path<? extends RoomEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoomEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoomEntity(PathMetadata metadata, PathInits inits) {
        this(RoomEntity.class, metadata, inits);
    }

    public QRoomEntity(Class<? extends RoomEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dateTime = inits.isInitialized("dateTime") ? new QDateTime(forProperty("dateTime")) : null;
        this.host = inits.isInitialized("host") ? new com.leesfamily.chuno.user.model.QUserEntity(forProperty("host"), inits.get("host")) : null;
    }

}

