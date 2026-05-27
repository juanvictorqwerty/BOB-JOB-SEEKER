package com.jobseeker.server.authentication.login;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jobseeker.server.authentication.dto.AuthDto;

import jakarta.validation.Valid;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<AuthDto> login(@RequestBody @Valid LoginValidation loginValidation) {
        return loginService.login(loginValidation);
    }
}
