package com.mingi.backend.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1338961235L;

    public static final QUser user = new QUser("user");

    public final ListPath<Comment, QComment> comments = this.<Comment, QComment>createList("comments", Comment.class, QComment.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final StringPath id = createString("id");

    public final StringPath isDelete = createString("isDelete");

    public final StringPath isSuspension = createString("isSuspension");

    public final DatePath<java.time.LocalDate> joinDate = createDate("joinDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> lastLoginDate = createDate("lastLoginDate", java.time.LocalDate.class);

    public final StringPath password = createString("password");

    public final ListPath<Post, QPost> posts = this.<Post, QPost>createList("posts", Post.class, QPost.class, PathInits.DIRECT2);

    public final StringPath profileImage = createString("profileImage");

    public final NumberPath<Long> profileProfileKey = createNumber("profileProfileKey", Long.class);

    public final StringPath role = createString("role");

    public final NumberPath<Long> userKey = createNumber("userKey", Long.class);

    public final StringPath usertype = createString("usertype");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

