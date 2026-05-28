package com.jobseeker.server.seeInfo.user;

import com.jobseeker.server.seeInfo.dto.UserInfo;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class SeeUserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<Optional<UserInfo>> getCurrentUser() {
        Optional<UserInfo> userInfo = userService.getUserInfoFromToken();
        return ResponseEntity.ok(userInfo);
    }
}