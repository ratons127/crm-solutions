package com.betopia.hrm.webapp.util;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class UploadFiles {

    private final S3Service s3Service;

    public UploadFiles(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public <T> ResponseEntity<GlobalResponse> upload(
            MultipartFile file,
            String folder,
            Function<String, T> entityUpdater
    ) {
        try {
            // Convert MultipartFile → File
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);

            // Build S3 key
            String imageKey = folder + "/" + file.getOriginalFilename();

            // Upload to S3
            String imageUrl = s3Service.uploadFile(imageKey, convFile);

            // Let caller update entity (repo find + set image)
            T updatedEntity = entityUpdater.apply(imageUrl);

            return ResponseBuilder.ok(updatedEntity, "Uploaded successfully");

        } catch (Exception e) {
            return ResponseBuilder.error("Upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public <T> ResponseEntity<GlobalResponse> delete(
            Long entityId,
            Supplier<T> entityFetcher,               // Fetch entity
            Consumer<T> imageCleaner                 // Clean image
    ) {
        try {
            // Fetch entity (throws if not found)
            T entity = entityFetcher.get();

            // Let caller handle DB cleanup (nullify image fields + save)
            imageCleaner.accept(entity);

            return ResponseBuilder.ok(null, "Image deleted successfully");

        } catch (Exception e) {
            return ResponseBuilder.error("Failed to delete image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteFromS3(String imageKey) {
        if (imageKey != null) {
            s3Service.deleteFile(imageKey);
        }
    }
}
