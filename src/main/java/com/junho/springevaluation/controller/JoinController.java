package com.junho.springevaluation.controller;

import com.junho.springevaluation.data.dto.AuthenDTO;
import com.junho.springevaluation.service.JoinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated // 유효성 검사를 위한 애너테이션
public class JoinController {
    private final JoinService joinService;

    @PostMapping(value = "/join")
    public ResponseEntity<String> join(@Valid @RequestBody AuthenDTO authenDTO) {
        if(this.joinService.join(authenDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("가입성공");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용중인 아이디입니다.");
    }

    @GetMapping(value = "/admin")
    public ResponseEntity<String> admin() {
        return ResponseEntity.status(HttpStatus.OK).body("관리자 인증이 확인되었습니다.");
    }
}
