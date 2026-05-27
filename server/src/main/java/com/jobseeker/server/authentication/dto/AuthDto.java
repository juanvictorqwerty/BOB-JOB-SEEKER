package com.jobseeker.server.authentication.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthDto {
    private boolean success;
    private String token;
    private String message;
    private Integer rank;
}