package com.jobseeker.server.authentication.tokens.revoque.allExcpetOne;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@RestController
public class KeepOneController {
    private final KeepOneService keepOneService;

    public KeepOneController(KeepOneService keepOneService) {
        this.keepOneService = keepOneService;
    }

    @PostMapping("/api/auth/keep-one")
    public String keepOne(@Valid @RequestBody KeepOneValidation keepOneValidation) {
        return keepOneService.keepOne(keepOneValidation);
    }
}
