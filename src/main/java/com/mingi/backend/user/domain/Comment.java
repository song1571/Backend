package com.mingi.backend.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString(exclude = {"writer", "post"})
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long commentId;

    private String content;

    @Column(name = "postDate")
    private LocalDateTime postDate;

    @Column(name = "is_delete")
    private String isDelete;


    @ManyToOne
    @JoinColumn(name = "post_post_id")
    @JsonIgnore
    private Post post;

    @ManyToOne
    @JoinColumn(name = "writer_user_key")
    @JsonIgnore
    private User writer;
}
