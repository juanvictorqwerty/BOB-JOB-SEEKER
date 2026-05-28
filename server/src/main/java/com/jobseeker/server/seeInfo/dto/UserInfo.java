package com.jobseeker.server.seeInfo.dto;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
    private boolean success;
    private String email;
    private String username;
    private int userRank;
    private Instant createdAt;
    private Instant updatedAt;
}