package com.mingi.backend.user.repository;

import com.mingi.backend.user.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long>, QuerydslPredicateExecutor<Token> {

}
