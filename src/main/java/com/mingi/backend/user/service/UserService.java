package com.mingi.backend.user.service;

import com.mingi.backend.S3ImageUploader;
import com.mingi.backend.security.PasswordUtil;
import com.mingi.backend.user.domain.*;
import com.mingi.backend.user.repository.ProfileRepository;
import com.mingi.backend.user.repository.TokenRepository;
import com.mingi.backend.user.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
public class UserService {
    private final JPAQueryFactory queryFactory;
    private final PasswordEncoder passwordEncoder;

    @Autowired // UserRepository와 자동연결
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private S3ImageUploader s3ImageUploader;

    public UserService(JPAQueryFactory queryFactory, PasswordEncoder passwordEncoder) {
        this.queryFactory = queryFactory;
        this.passwordEncoder = passwordEncoder;
    }

    // ID로 DB에 해당 ID가 있는지 확인
    public String findUserIdById(String id) {
        QUser user = QUser.user;
        return queryFactory
                .select(user.id)
                .from(user)
                .where(user.id.eq(id))
                .fetchOne();
    }

    // ID로 DB에서 패스워드 가져오기
    public String findUserPasswordById(String id) {
        QUser user = QUser.user;
        return queryFactory
                .select(user.password)
                .from(user)
                .where(user.id.eq(id))
                .fetchOne();
    }

    // 회원가입때 중복 이메일인지 확인
    public String findUserMailByMail(String mail) {
        QUser user = QUser.user;
        return queryFactory
                .select(user.email)
                .from(user)
                .where(user.email.eq(mail))
                .fetchOne();
    }

    // 아이디 확인
    public String checkId(User userData) {
        return findUserIdById(userData.getId());
    }

    // 비밀번호 확인
    public boolean checkPW(User userData) {
        String findUserPassword = findUserPasswordById(userData.getId());
        PasswordUtil passwordUtil = new PasswordUtil();
        return passwordUtil.checkPassword(userData.getPassword(), findUserPassword);
    }

    // Id로 프로필 이미지 가져오기
    public String findUserProfileImageById(String id) {
        QUser user = QUser.user;
        return queryFactory
                .select(user.profileImage)
                .from(user)
                .where(user.id.eq(id))
                .fetchOne();
    }

    // 이메일로 프로필 이미지 가져오기
    public Long findUserProfileIDByEmail(String email) {
        QProfile profile = QProfile.profile;
        return queryFactory
                .select(profile.profileKey)
                .from(profile)
                .where(profile.email.eq(email))
                .fetchOne();
    }

    public void saveUser(User user, MultipartFile image) {
        user.setProfileImage(s3ImageUploader.uploadImage(image));
        user.setPassword(makePassword(user.getPassword()));
        user.setJoinDate(LocalDate.now());
        user.setLastLoginDate(LocalDate.now());
        user.setIsDelete("N");
        user.setIsSuspension("N");
        user.setRole("USER");
        user.setUsertype("GENERAL_USER");
        user.setProfileProfileKey(findUserProfileIDByEmail(user.getEmail()));
        userRepository.save(user);
    }

    public void saveUserProfile(User user) {
        Profile profile = new Profile();
        profile.setEmail(user.getEmail());
        profile.setOptions(1);
        profileRepository.save(profile);
    }

    public void saveRefreshToken(String userName, String refreshToken) {
        Token token = new Token();
        token.setKeyEmail(userName);
        token.setToken(refreshToken);
        tokenRepository.save(token);
    }

    // 비밀번호를 암호화하여 반환
    public String makePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // ID로 DB에 해당 토큰이 있는지 확인
    public String findRefreshToken (String userName) {
        QToken token1 = QToken.token1;
        return queryFactory
                .select(token1.token)
                .from(token1)
                .where(token1.keyEmail.eq(userName))
                .fetchOne();
    }

    // ID로 DB에 있는 유저 키를 반환
    public Long getUserKey(String userId) {
        QUser user = QUser.user;
        return queryFactory
                .select(user.userKey)
                .from(user)
                .where(user.id.eq(userId))
                .fetchOne();
    }
}
