package com.mingi.backend.user.controller;

import com.mingi.backend.JwtTokenProvider;
import com.mingi.backend.ResponseMessage;
import com.mingi.backend.user.domain.User;
import com.mingi.backend.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Objects;

@RestController // JSON 형태로 객체 데이터를 반환
public class LoginController {

    @Autowired // UserService와 자동연결
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/logins")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        String message;
        String refreshToken = "";
        if(Objects.equals(user.getId(), userService.checkId(user))) {
            if (userService.checkPW(user)) {
                message = "로그인에 성공하였습니다.";
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());  // 세션에 유저 ID를 저장
                session.setAttribute("userKey", userService.getUserKey(user.getId())); // 세션에 유저 키 저장
                
                Cookie[] cookies = request.getCookies(); // 모든 쿠키를 가져옴
                String refreshTokenCookie = "";
                if(cookies != null) {
                    refreshTokenCookie = Arrays.stream(cookies) // Stream으로 특정 쿠키를 탐색
                            .filter(cookie -> "refreshToken".equals(cookie.getName()))
                            .map(Cookie::getValue)
                            .findFirst()
                            .orElse("Cookie not found");
                }
                
                refreshToken = userService.findRefreshToken(user.getId());
                if(refreshToken == null || refreshToken.isBlank()) { // DB에 해당 유저의 토큰이 없다.
                    Cookie cookie = new Cookie("refreshToken", refreshToken);
                    cookie.setMaxAge(3600);
                    cookie.setPath("/");
                    response.addCookie(cookie); // refreshToken을 브라우저에 저장
                    userService.saveRefreshToken(user.getId(), jwtTokenProvider.createRefreshToken(user.getId()));
                }

                // DB에 해당 유저의 토큰이 있지만 브라우저 토큰과 불일치 / 브라우저에 토큰이 없다.
                else if(!Objects.equals(refreshTokenCookie, refreshToken) || refreshTokenCookie.equals("Cookie not found")) {
                    Cookie cookie = new Cookie("refreshToken", refreshToken);
                    cookie.setMaxAge(3600);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    System.out.println("refreshToken이 갱신되었습니다.");
                }
                else {
                    System.out.println("refreshToken이 동일합니다.");
                }
            }
            else {
                message = "로그인에 실패하였습니다. 비밀번호가 다릅니다";
            }
        }
        else {
            message = "로그인에 실패하였습니다. 해당하는 아이디가 없습니다";
        }
        return ResponseEntity.ok().body(new ResponseMessage(message));
    }

    @GetMapping("/users")
    public ResponseEntity<?> loginNoticeBoard (HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if(userId == null || userId.isBlank()) {
            return ResponseEntity.unprocessableEntity().body("");
        }
        else {
            String loginData = "{\"data\": { " +
                    "\"id\": \"" + userId + "\", " +
                    "\"options\": {\"notified\": true}, " +
                    "\"profileImage\": \"" + userService.findUserProfileImageById(userId) + "\"}}";
            return ResponseEntity.ok(loginData);
        }
    }

    @PostMapping("/user/logout")
    public ResponseEntity<?> logoutNoticeBoard (HttpServletResponse response, HttpServletRequest request) {

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie); // 쿠키 JSESSIONID 삭제

        HttpSession session = request.getSession();
        session.invalidate(); // 세션을 종료
        return ResponseEntity.ok("로그아웃 완료");
    }
}
