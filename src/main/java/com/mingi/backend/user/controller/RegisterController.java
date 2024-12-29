package com.mingi.backend.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mingi.backend.SMTP.MailRequest;
import com.mingi.backend.ResponseMessage;
import com.mingi.backend.SMTP.MailService;
import com.mingi.backend.UUID.VerificationCodeGenerator;
import com.mingi.backend.user.domain.User;
import com.mingi.backend.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @GetMapping("/users/{email}/{id}") // 사용중인 아이디인지 이메일인지 확인
    public ResponseEntity<?> registerMailSendAlert(@PathVariable String email, @PathVariable String id) {

        if(Objects.equals(id, userService.findUserIdById(id))) {
            return ResponseEntity.unprocessableEntity().body(new ResponseMessage("이미 사용중인 아이디입니다."));
        }
        else if(userService.findUserMailByMail(email) != null) {
            return ResponseEntity.unprocessableEntity().body(new ResponseMessage("이미 사용중인 이메일입니다."));
        }
        else {
            return ResponseEntity.ok("");
        }
    }

    @PostMapping("/mail/send") // 지정한 이메일로 이메일을 전송
    public ResponseEntity<?> registerMailSend(@RequestBody User user, HttpServletRequest request)
            throws JsonProcessingException {

        String code = VerificationCodeGenerator.generatedVerificationCode(); // UUID로 인증번호 생성
        HttpSession session = request.getSession(); // 생성한 인증 코드를 세선에 저장
        session.setAttribute("sessionCode", code);
        session.setMaxInactiveInterval(3 * 60); // 데이터 유지시간 3분

        String codeMessage = "인증번호는 [" + code + "] 입니다. 인증코드는 3분만 유지됩니다.";
        mailService.sendMail(user.getEmail(), "NoticeBoard 인증번호", codeMessage);
        return ResponseEntity.ok("");
    }

    @PostMapping("/mail/check")
    public ResponseEntity<?> registerMailCheck(@RequestBody MailRequest mailRequest, HttpSession session,
                                               HttpServletRequest request, HttpServletResponse response) {
        String sessionCode = (String) session.getAttribute("sessionCode");

        if(mailRequest.getCode() == null || mailRequest.getCode().isBlank()) {
            return ResponseEntity.unprocessableEntity().body("인증번호가 입력되지 않았습니다.");
        }
        else if(Objects.equals(mailRequest.getCode(), sessionCode)) {
            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie); // 쿠키 JSESSIONID 삭제
            session.invalidate(); // 인증 코드가 있는 세션을 종료
            return ResponseEntity.ok("인증 성공");
        }
        else {
            return ResponseEntity.unprocessableEntity().body(new ResponseMessage("인증번호가 일치하지 않습니다."));
        }
    }

    @PostMapping("/registers") // 계정 생성 단계
    public ResponseEntity<?> register(@RequestPart("data") User user, @RequestPart("image") MultipartFile image) {
        userService.saveUserProfile(user);
        userService.saveUser(user, image);
        return ResponseEntity.ok().body(new ResponseMessage("계정이 생성되었습니다."));
    }
}
