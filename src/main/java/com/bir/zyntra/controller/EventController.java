package com.bir.zyntra.controller;

import com.bir.zyntra.dto.response.ApiResponse;
import com.bir.zyntra.model.Event;
import com.bir.zyntra.model.UserAccount;
import com.bir.zyntra.service.job.EventService;
import com.bir.zyntra.service.storage.StorageService;
import com.bir.zyntra.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class EventController {

    private final EventService jobService;

    private final UserService userService;

    @PostMapping("/create")
    public ApiResponse createJob(
            @RequestParam String email,
            @RequestParam String driveUrl
    ) {
        UserAccount user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            Event job = jobService.createEvent(user, driveUrl);

            return ApiResponse.builder()
                    .success(true)
                    .message("Event created successfully")
                    .data(job)
                    .build();
        } catch (Exception e) {
            System.out.println(e);

            return ApiResponse.builder()
                    .success(false)
                    .message("Event created failed : " + e)
                    .build();
        }
    }

//    @GetMapping("/user/{userId}")
//    public List<Event> getUserJobs(@PathVariable UUID userId) {
//        UserAccount user = userService.findById(userId).orElseThrow();
//        return jobService.getJobsByUser(user.getId());
//    }
//
//    @GetMapping("/{jobId}")
//    public Event getJob(@PathVariable Long jobId) {
//        return jobService.getJob(jobId);
//    }
}
