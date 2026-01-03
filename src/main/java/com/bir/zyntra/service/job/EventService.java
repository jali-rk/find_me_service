package com.bir.zyntra.service.job;

import com.bir.zyntra.batch.image.AsyncEventTasks;
import com.bir.zyntra.model.Event;
import com.bir.zyntra.model.UserAccount;
import com.bir.zyntra.model.enums.JobStatus;
import com.bir.zyntra.repository.EventRepository;
import com.bir.zyntra.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final JobOperator jobOperator;
    private final Job imageJob;
    private final StorageService gDriveService;
    private final AsyncEventTasks asyncEventTasks;

    @Transactional
    public Event createEventInTransaction(UserAccount user, String folderUrl) {
        Event event = Event.builder()
                .user(user)
                .folderUrl(folderUrl)
                .status(JobStatus.PENDING)
                .build();

        return eventRepository.save(event);
    }

    public Event createEvent(UserAccount user, String folderUrl) throws Exception {
        Event event = createEventInTransaction(user, folderUrl);

        asyncEventTasks.startImageJob(event);

        asyncEventTasks.updateImageCount(event.getId(), folderUrl);

        return event;
    }
}
