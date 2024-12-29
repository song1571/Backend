package com.mingi.backend.user.repository;

import com.mingi.backend.user.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TagRepository extends JpaRepository<Tag, Long>, QuerydslPredicateExecutor<Tag> {

}
