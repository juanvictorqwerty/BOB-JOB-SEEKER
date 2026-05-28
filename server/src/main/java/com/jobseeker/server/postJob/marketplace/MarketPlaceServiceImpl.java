package com.jobseeker.server.postJob.marketplace;

import com.jobseeker.server.models.MarketPlace;
import com.jobseeker.server.models.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class MarketPlaceServiceImpl implements MarketPlaceService {

    private final Interface marketPlaceRepository;
    private final UserRepo usersRepository;
    private final ObjectMapper objectMapper;

    // 1. DYNAMIC ROOT RESOLUTION: Resolves to where your .mvn folder / pom.xml is
    private static final String PROJECT_ROOT = System.getProperty("user.dir");
    private static final String UPLOAD_DIR = "images";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public MarketPlaceServiceImpl(Interface marketPlaceRepository,
            UserRepo usersRepository,
            ObjectMapper objectMapper) {
        this.marketPlaceRepository = marketPlaceRepository;
        this.usersRepository = usersRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public MarketPlace createListing(Validation request, List<MultipartFile> images, UUID postingUserId) {

        if (images != null && images.size() > 2) {
            throw new IllegalArgumentException("You can upload a maximum of 2 images.");
        }

        Users postingUser = usersRepository.findById(postingUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + postingUserId));

        String imageUrlJson = null;
        if (images != null && !images.isEmpty()) {
            ArrayNode jsonArray = objectMapper.createArrayNode();

            // 2. Combine project root with the target folder safely
            Path targetDirectoryPath = Paths.get(PROJECT_ROOT, UPLOAD_DIR);
            File directory = targetDirectoryPath.toFile();

            // This will now successfully create the folder inside your workspace root
            if (!directory.exists()) {
                directory.mkdirs();
            }

            for (MultipartFile file : images) {
                if (file.isEmpty())
                    continue;

                if (file.getSize() > MAX_FILE_SIZE) {
                    throw new IllegalArgumentException(
                            "File size exceeds the maximum limit of 5MB: " + file.getOriginalFilename());
                }

                try {
                    String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                    // 3. Target the resolved path file path stream
                    Path filePath = targetDirectoryPath.resolve(uniqueFileName);

                    // Save file binary stream to disk
                    Files.copy(file.getInputStream(), filePath);

                    // Push the absolute string path to your JSON DB string tracker
                    jsonArray.add(filePath.toString());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image locally", e);
                }
            }
            imageUrlJson = jsonArray.toString();
        }

        MarketPlace marketPlace = MarketPlace.builder()
                .title(request.getTitle())
                .location(request.getLocation())
                .description(request.getDescription())
                .imageUrl(imageUrlJson)
                .postingUser(postingUser)
                .isOpen(true)
                .build();

        return marketPlaceRepository.save(marketPlace);
    }
}