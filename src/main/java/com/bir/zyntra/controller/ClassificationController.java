package com.bir.zyntra.controller;

import com.bir.zyntra.model.ClassificationResult;
import com.bir.zyntra.model.Image;
import com.bir.zyntra.service.job.ClassificationService;
import com.bir.zyntra.service.job.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/classification")
@RequiredArgsConstructor
public class ClassificationController {

    private final ClassificationService classificationService;

    private final ImageService imageService;

    @GetMapping("/{imageId}")
    public ClassificationResult getResult(@PathVariable Long imageId) {
        Image image = imageService.get(imageId);
        return image.getResult();
    }

    @PostMapping("/save")
    public ClassificationResult saveResult(
            @RequestParam Long imageId,
            @RequestParam String label,
            @RequestParam Double confidence,
            @RequestBody(required = false) String rawJson
    ) {
        Image image = imageService.get(imageId);
        return classificationService.saveResult(image, label, confidence, rawJson);
    }
}
