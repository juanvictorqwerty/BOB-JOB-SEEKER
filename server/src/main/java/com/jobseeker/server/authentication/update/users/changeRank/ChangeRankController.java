package com.jobseeker.server.authentication.update.users.changeRank;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class ChangeRankController {
    private final ChangeRankService changeRankService;

    public ChangeRankController(ChangeRankService changeRankService) {
        this.changeRankService = changeRankService;
    }

    @PostMapping("api/change-rank")
    public String changeRank(@Valid @RequestBody ChangeRankValidation changeRankValidation) {
        return changeRankService.changeRank(changeRankValidation);
    }
}
