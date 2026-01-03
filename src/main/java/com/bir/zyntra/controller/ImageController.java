package com.bir.zyntra.controller;

import com.bir.zyntra.model.Image;
import com.bir.zyntra.service.job.ImageService;
import com.bir.zyntra.service.job.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    private final EventService eventService;

    @GetMapping("/pending")
    public List<Image> getPendingImages() {
        return imageService.getPendingItems();
    }

    @GetMapping("/{id}")
    public Image getImage(@PathVariable Long id) {
        return imageService.get(id);
    }

//    @GetMapping("/job/{jobId}")
//    public List<Image> getImagesForJob(@PathVariable Long jobId) {
//
//        return eventService.getJob(jobId).getImages();
//    }
}
