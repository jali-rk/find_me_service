package com.bir.zyntra.model;

import com.bir.zyntra.model.enums.ImageStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "image")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = true)
    private Event job;

    @Column(nullable = false)
    private String originalUrl;

    private String localPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageStatus status;

    private int retryCount;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "image", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ClassificationResult result;
}
