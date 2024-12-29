package com.mingi.backend.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPostTagId is a Querydsl query type for PostTagId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPostTagId extends BeanPath<PostTagId> {

    private static final long serialVersionUID = -1022174963L;

    public static final QPostTagId postTagId = new QPostTagId("postTagId");

    public final NumberPath<Long> postPostId = createNumber("postPostId", Long.class);

    public final NumberPath<Long> tagTagId = createNumber("tagTagId", Long.class);

    public QPostTagId(String variable) {
        super(PostTagId.class, forVariable(variable));
    }

    public QPostTagId(Path<? extends PostTagId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPostTagId(PathMetadata metadata) {
        super(PostTagId.class, metadata);
    }

}

