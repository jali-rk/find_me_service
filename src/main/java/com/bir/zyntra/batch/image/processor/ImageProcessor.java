package com.bir.zyntra.batch.image.processor;

import com.bir.zyntra.model.Event;
import com.bir.zyntra.model.Image;
import com.bir.zyntra.model.enums.ImageStatus;
import com.bir.zyntra.service.storage.s3.S3Service;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class ImageProcessor implements ItemProcessor<File, Image> {

    private final Drive drive;
    private final S3Service s3Service;
    private final EntityManager entityManager;

    @Value("#{jobParameters['eventId']}")
    private String eventId;

    @Override
    public Image process(File googleFile) {
        try {
            log.info("Processing file: {}", googleFile.getName());

            InputStream driveInputStream = drive.files()
                    .get(googleFile.getId())
                    .executeMediaAsInputStream();

            long size = googleFile.getSize() != null ? googleFile.getSize() : -1;

            String s3Url;

            if (size == -1) {
                byte[] bytes = driveInputStream.readAllBytes();
                s3Url = s3Service.uploadBytes(bytes, googleFile.getName(), googleFile.getMimeType());
            } else {
                s3Url = s3Service.uploadFile(driveInputStream, googleFile.getName(), size, googleFile.getMimeType());
            }

            Event eventReference = entityManager.getReference(Event.class, UUID.fromString(eventId));

            return Image.builder()
                    .job(eventReference)
                    .originalUrl(s3Url)
                    .localPath(null)
                    .status(ImageStatus.UPLOADED)
                    .retryCount(0)
                    .build();

        } catch (Exception e) {
            log.error("Failed to process file {}: {}", googleFile.getId(), e.getMessage());

            Event eventReference = entityManager.getReference(Event.class, UUID.fromString(eventId));
            return Image.builder()
                    .job(eventReference)
                    .originalUrl("PENDING")
                    .status(ImageStatus.FAILED)
                    .errorMessage(e.getMessage())
                    .build();
        }
    }
}