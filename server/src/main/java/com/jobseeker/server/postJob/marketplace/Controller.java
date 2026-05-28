package com.jobseeker.server.postJob.marketplace;

import com.jobseeker.server.models.MarketPlace;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/add/marketplace")
public class Controller {

    private final MarketPlaceService marketPlaceService;

    public Controller(MarketPlaceService marketPlaceService) {
        this.marketPlaceService = marketPlaceService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MarketPlace> createPost(
            @Valid @RequestPart("data") Validation request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal String userIdStr) {

        if (userIdStr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UUID postingUserId = UUID.fromString(userIdStr);
        MarketPlace post = marketPlaceService.createListing(request, images, postingUserId);

        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }
}