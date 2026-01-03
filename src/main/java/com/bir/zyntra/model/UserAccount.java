package com.bir.zyntra.model;

import com.bir.zyntra.model.enums.AuthProvider;
import com.bir.zyntra.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_account")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @Column(nullable = false)
    private String providerUserId;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserRole role = UserRole.USER;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
