package com.jobseeker.server.JobSearch;

public record JobSearchValidation(
        String q,
        int page,
        int size) {
}