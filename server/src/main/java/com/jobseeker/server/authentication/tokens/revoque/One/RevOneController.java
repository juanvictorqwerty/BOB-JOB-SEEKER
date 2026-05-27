package com.jobseeker.server.authentication.tokens.revoque.One;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
public class RevOneController {
    private final RevOneService revOneService;

    public RevOneController(RevOneService revOneService) {
        this.revOneService = revOneService;
    }

    @PostMapping("/api/auth/revoke-one")
    public String revOneToken(@RequestBody @Valid RevOneValidation revOneValidation) {
        return revOneService.revOneToken(revOneValidation);
    }

}
