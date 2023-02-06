package com.leesfamily.chuno.user.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserProfile is a Querydsl query type for UserProfile
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUserProfile extends BeanPath<UserProfile> {

    private static final long serialVersionUID = 1757465161L;

    public static final QUserProfile userProfile = new QUserProfile("userProfile");

    public final StringPath path = createString("path");

    public final StringPath saveName = createString("saveName");

    public QUserProfile(String variable) {
        super(UserProfile.class, forVariable(variable));
    }

    public QUserProfile(Path<? extends UserProfile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserProfile(PathMetadata metadata) {
        super(UserProfile.class, metadata);
    }

}

