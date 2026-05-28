package com.jobseeker.server.postJob.marketplace;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class ImageServeController {

    // Must match the UPLOAD_DIR used in MarketPlaceServiceImpl
    private static final String PROJECT_ROOT = System.getProperty("user.dir");
    private static final String UPLOAD_DIR = "images";

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        // Sanitize: strip any path traversal attempts (e.g. "../../etc/passwd")
        String safeName = Paths.get(filename).getFileName().toString();

        Path imagePath = Paths.get(PROJECT_ROOT, UPLOAD_DIR, safeName);

        if (!Files.exists(imagePath) || !Files.isReadable(imagePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Detect content type from file extension (jpg, png, webp, etc.)
        String contentType;
        try {
            contentType = Files.probeContentType(imagePath);
        } catch (Exception e) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        Resource resource = new FileSystemResource(imagePath);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(resource);
    }
}
