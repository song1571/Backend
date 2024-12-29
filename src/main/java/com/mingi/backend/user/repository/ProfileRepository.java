package com.mingi.backend.user.repository;

import com.mingi.backend.user.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProfileRepository extends JpaRepository<Profile, Long>, QuerydslPredicateExecutor<Profile> {
}
