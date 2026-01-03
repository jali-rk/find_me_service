package com.bir.zyntra.service.job;

import com.bir.zyntra.model.ClassificationResult;
import com.bir.zyntra.model.Image;
import com.bir.zyntra.repository.ClassificationResultRepository;
import com.bir.zyntra.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassificationService {

    private final ClassificationResultRepository resultRepo;

    private final ImageRepository itemRepo;

    @Transactional
    public ClassificationResult saveResult(Image item, String label, Double confidence, String rawResponse) {
        ClassificationResult result = ClassificationResult.builder()
                .image(item)
                .predictedLabel(label)
                .confidence(confidence)
                .rawResponse(rawResponse)
                .classifiedAt(LocalDateTime.now())
                .build();

        item.setResult(result);
        itemRepo.save(item);

        return resultRepo.save(result);
    }
}
