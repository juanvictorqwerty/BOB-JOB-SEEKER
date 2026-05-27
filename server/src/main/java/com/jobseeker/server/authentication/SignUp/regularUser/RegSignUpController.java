package com.jobseeker.server.authentication.SignUp.regularUser;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jobseeker.server.authentication.dto.AuthDto;

import jakarta.validation.Valid;

@RestController
public class RegSignUpController {
    private final RegSignUpService regSignUpService;

    public RegSignUpController(RegSignUpService regSignUpService) {
        this.regSignUpService = regSignUpService;
    }

    @PostMapping("/api/auth/register/regular")
    public ResponseEntity<AuthDto> register(@Valid @RequestBody RegSignUpValidation regSignUpValidation) {

        return regSignUpService.register(regSignUpValidation);
    }
}
