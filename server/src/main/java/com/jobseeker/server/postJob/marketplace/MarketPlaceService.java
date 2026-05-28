package com.jobseeker.server.postJob.marketplace;

import com.jobseeker.server.models.MarketPlace;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

public interface MarketPlaceService {
    MarketPlace createListing(MarketPlaceValidation request, List<MultipartFile> images, UUID postingUserId);
}