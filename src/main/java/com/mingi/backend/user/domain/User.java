package com.mingi.backend.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = "posts")
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userKey;

    private String id;

    private String password;

    @Email
    private String email;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "last_login_date")
    private LocalDate lastLoginDate;

    @Column(name = "is_delete")
    private String isDelete;

    @Column(name = "is_suspension")
    private String isSuspension;

    private String role;

    @Column(name = "user_type")
    private String usertype;

    @Column(name = "profile_profile_key")
    private Long profileProfileKey;

    @OneToMany(mappedBy = "writer")
    @JsonIgnore // 순환 참조를 방지. 없으면 무한 참조됨
    private List<Post> posts;

    @OneToMany(mappedBy = "writer")
    @JsonIgnore
    private List<Comment> comments;

}
