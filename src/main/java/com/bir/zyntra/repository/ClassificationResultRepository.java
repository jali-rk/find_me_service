package com.bir.zyntra.repository;

import com.bir.zyntra.model.ClassificationResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassificationResultRepository extends JpaRepository<ClassificationResult, Long> {
    boolean existsByImageId(Long imageId);
}
