package com.jobseeker.server.JobSearch;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class JobSearchController {

    private final JobSearchService searchService;

    public JobSearchController(JobSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/unified")
    public ResponseEntity<DTO> searchAll(
            @RequestParam(value = "q", required = false, defaultValue = "") String textQuery,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        DTO multiTableResults = searchService.unifiedSearch(textQuery, page, size);
        return ResponseEntity.ok(multiTableResults);
    }
}