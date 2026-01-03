package com.bir.zyntra.batch.image;

import com.bir.zyntra.model.Event;
import com.bir.zyntra.repository.EventRepository;
import com.bir.zyntra.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AsyncEventTasks {

    private final JobOperator jobOperator;
    private final Job imageJob;
    private final EventRepository eventRepository;
    private final StorageService gDriveService;

    @Async
    public void startImageJob(Event event) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("eventId", event.getId().toString())
                .addString("folderUrl", event.getFolderUrl())
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        jobOperator.start(imageJob, jobParameters);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateImageCount(UUID eventId, String folderUrl) {
        try {
            int count = gDriveService.getNumberOfFilesInFolder(folderUrl);
            eventRepository.findById(eventId).ifPresent(event -> {
                event.setTotalImages(count);
                eventRepository.save(event);
            });
        } catch (Exception e) {
            eventRepository.findById(eventId).ifPresent(event -> {
                event.setErrorMessage("Failed to fetch image count: " + e.getMessage());
                eventRepository.save(event);
            });
        }
    }
}
