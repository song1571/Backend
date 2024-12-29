package com.mingi.backend.user.repository;

import com.mingi.backend.user.domain.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PostTagRepository extends JpaRepository<PostTag, Long>, QuerydslPredicateExecutor<PostTag> {
}
