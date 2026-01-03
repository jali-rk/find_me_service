package com.bir.zyntra.batch.image;

import com.bir.zyntra.model.enums.JobStatus;
import com.bir.zyntra.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageJobListener implements JobExecutionListener {

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public void beforeJob(JobExecution jobExecution) {
        UUID eventId = UUID.fromString(
                Objects.requireNonNull(jobExecution.getJobParameters().getString("eventId"))
        );

        eventRepository.updateStatus(eventId, JobStatus.DOWNLOADING);
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        UUID eventId = UUID.fromString(
                Objects.requireNonNull(jobExecution.getJobParameters().getString("eventId"))
        );

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            eventRepository.updateStatus(eventId, JobStatus.COMPLETED);
        } else {
            eventRepository.updateStatus(
                    eventId,
                    JobStatus.FAILED
            );
        }
    }
}
