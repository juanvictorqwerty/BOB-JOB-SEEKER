package com.jobseeker.server.authentication.tokens.revoque.all;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
public class AllRevokeController {
    private final AllRevokeService allRevokeService;

    public AllRevokeController(AllRevokeService allRevokeService) {
        this.allRevokeService = allRevokeService;
    }

    @PostMapping("/api/revoke-all")
    public String revAllToken(@Valid @RequestBody AllRevokeValidation allRevokeValidation) {
        return allRevokeService.revokeAllTokens(allRevokeValidation);
    }
}
