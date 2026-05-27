package com.jobseeker.server.authentication.forgotPassword.recoverAccount;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class RecoverAccountController {
    private final RecoverAccountService recoverAccountService;

    public RecoverAccountController(RecoverAccountService recoverAccountService) {
        this.recoverAccountService = recoverAccountService;
    }

    @PostMapping("/api/recover-account/{token}")
    public String recoverAccount(
            @Valid @RequestBody RecoverAccountValidation recoverAccountValidation,
            @PathVariable String token) {
        return recoverAccountService.recoverAccount(recoverAccountValidation, token);
    }
}
