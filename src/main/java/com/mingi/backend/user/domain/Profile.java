package com.mingi.backend.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_key")
    private long profileKey;

    @Email
    private String email;

    private int options;

    private String phone;

}
