package com.bir.zyntra.service.job;

import com.bir.zyntra.model.Image;
import com.bir.zyntra.model.Event;
import com.bir.zyntra.model.enums.ImageStatus;
import com.bir.zyntra.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final ImageRepository itemRepo;

    @Transactional
    public Image createItem(Event job, String imageUrl) {
        Image item = Image.builder()
                .job(job)
                .originalUrl(imageUrl)
                .status(ImageStatus.PENDING)
                .retryCount(0)
                .build();

        return itemRepo.save(item);
    }

    public List<Image> getPendingItems() {
        return itemRepo.findByStatus(ImageStatus.PENDING);
    }

    public Image get(Long id) {
        return itemRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Image item not found: " + id));
    }

    @Transactional
    public void updateStatus(Long id, ImageStatus status) {
        Image item = get(id);
        item.setStatus(status);
        itemRepo.save(item);
    }

    @Transactional
    public void incrementRetry(Long id) {
        Image item = get(id);
        item.setRetryCount(item.getRetryCount() + 1);
        itemRepo.save(item);
    }

    @Transactional
    public void markError(Long id, String message) {
        Image item = get(id);
        item.setStatus(ImageStatus.ERROR);
        item.setErrorMessage(message);
        itemRepo.save(item);
    }
}
