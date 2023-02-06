package com.leesfamily.chuno.room.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDateTime is a Querydsl query type for DateTime
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QDateTime extends BeanPath<DateTime> {

    private static final long serialVersionUID = 1094506880L;

    public static final QDateTime dateTime = new QDateTime("dateTime");

    public final NumberPath<Integer> day = createNumber("day", Integer.class);

    public final NumberPath<Integer> hour = createNumber("hour", Integer.class);

    public final NumberPath<Integer> minute = createNumber("minute", Integer.class);

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QDateTime(String variable) {
        super(DateTime.class, forVariable(variable));
    }

    public QDateTime(Path<? extends DateTime> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDateTime(PathMetadata metadata) {
        super(DateTime.class, metadata);
    }

}

