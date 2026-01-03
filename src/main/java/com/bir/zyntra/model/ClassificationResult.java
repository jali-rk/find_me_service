package com.bir.zyntra.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "classification_result")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ClassificationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Column(nullable = false)
    private String predictedLabel;

    @Column(nullable = false)
    private Double confidence;

    @Column(columnDefinition = "JSON")
    private String rawResponse;

    private LocalDateTime classifiedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
