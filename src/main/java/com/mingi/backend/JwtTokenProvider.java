package com.mingi.backend;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenProvider {

    private static final String SECRET_KEY = "mingiNoticeBoard";

    public String createRefreshToken(String userName) {
        long expirationTime = 1000 * 60 * 60; // 1시간
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
}
