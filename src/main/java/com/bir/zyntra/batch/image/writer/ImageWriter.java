package com.bir.zyntra.batch.image.writer;

import com.bir.zyntra.model.Image;
import com.bir.zyntra.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageWriter implements ItemWriter<Image> {

    private final ImageRepository imageRepository;

    @Override
    public void write(Chunk<? extends Image> items) {
        imageRepository.saveAll(items);
    }
}
