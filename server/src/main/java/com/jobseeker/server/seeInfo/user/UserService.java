package com.jobseeker.server.seeInfo.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jobseeker.server.models.Users;
import com.jobseeker.server.seeInfo.dto.UserInfo;

@Service
public class UserService {

    @Autowired
    private SeeUserInterface seeUserInterface;

    public Optional<UserInfo> getUserInfoFromToken() {
        try {
            String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UUID id = UUID.fromString(principal);
            Optional<Users> userOptional = seeUserInterface.findById(id);

            if (userOptional.isPresent()) {
                Users user = userOptional.get();
                return Optional.of(UserInfo.builder()
                        .success(true)
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .userRank(user.getUserRank())
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .build());
            } else {
                return Optional.of(UserInfo.builder()
                        .success(false)
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace(); // Replaced System.out.println for better debug tracking
            return Optional.of(UserInfo.builder()
                    .success(false)
                    .build());
        }
    }
}