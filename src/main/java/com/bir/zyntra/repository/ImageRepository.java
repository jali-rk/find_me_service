package com.bir.zyntra.repository;

import com.bir.zyntra.model.Image;
import com.bir.zyntra.model.enums.ImageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByStatus(ImageStatus status);

    @Query("SELECT i FROM Image i WHERE i.status = :status ORDER BY i.id")
    List<Image> findItemsForBatch(ImageStatus status);

    long countByJobId(Long jobId);
}
