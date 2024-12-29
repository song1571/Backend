package com.mingi.backend.user.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PostTagId implements Serializable {
    private Long postPostId;
    private Long tagTagId;

}
