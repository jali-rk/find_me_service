package com.bir.zyntra.model;

import com.bir.zyntra.model.enums.JobStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "event")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(nullable = false, unique = true)
    private String folderUrl;

    private int totalImages;

    private int processedCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    private String errorMessage;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;
}
