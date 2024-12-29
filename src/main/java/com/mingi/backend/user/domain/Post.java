package com.mingi.backend.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = "writer")
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private long postId;

    private String title;

    private String content;

    private int likes;

    private int views;

    @Column(name = "is_block_comment")
    private String isBlockComment;

    @Column(name = "is_private")
    private String isPrivate;

    @Column(name = "is_delete")
    private String isDelete;

    @Column(name = "post_date")
    @JsonFormat(pattern = "yy-MM-dd HH:mm")
    private LocalDateTime postDate;

    @ManyToOne
    @JoinColumn(name = "writer_user_key")
    private User writer;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private List<PostTag> postTags;
}
