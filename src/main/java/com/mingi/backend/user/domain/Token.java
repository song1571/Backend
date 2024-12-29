package com.mingi.backend.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "refresh_token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long token_id;
    @Column(name = "key_email")
    private String keyEmail;
    private String token;
}