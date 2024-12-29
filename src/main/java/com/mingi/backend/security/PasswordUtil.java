package com.mingi.backend.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 비밀번호가 동일한 해시 값을 가지고 있는지 확인
    public boolean checkPassword(String rawPassword, String storedHash) {
        return passwordEncoder.matches(rawPassword, storedHash);
    }
}
