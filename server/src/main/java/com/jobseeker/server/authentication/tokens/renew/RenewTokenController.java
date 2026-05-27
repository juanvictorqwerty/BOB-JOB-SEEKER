package com.jobseeker.server.authentication.tokens.renew;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
public class RenewTokenController {
    private final RenewTokenService renewTokenService;

    public RenewTokenController(RenewTokenService renewTokenService) {
        this.renewTokenService = renewTokenService;
    }

    @PostMapping("/api/auth/token/renew")
    public String renewToken(@Valid @RequestBody RenewTokenValidation renewTokenValidation) {
        return renewTokenService.renewToken(renewTokenValidation);
    }
}
