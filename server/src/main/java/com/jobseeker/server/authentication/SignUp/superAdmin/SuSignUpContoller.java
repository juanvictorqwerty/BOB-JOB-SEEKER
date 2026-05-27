package com.jobseeker.server.authentication.SignUp.superAdmin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jobseeker.server.authentication.dto.AuthDto;

import jakarta.validation.Valid;

@RestController
public class SuSignUpContoller {
    private final SuSignUpService suSignUpService;

    public SuSignUpContoller(SuSignUpService suSignUpService) {
        this.suSignUpService = suSignUpService;
    }

    @PostMapping("/api/auth/register/superadmin")
    public ResponseEntity<AuthDto> register(@Valid @RequestBody SuSignUpValidation suSignUpValidation) {
        return suSignUpService.register(suSignUpValidation);
    }
}
