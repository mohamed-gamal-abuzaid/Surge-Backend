package com.Surge.surge_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "channels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "channel_name", nullable = false, unique = true, length = 50)
    private String channelName;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "banner_url", length = 500)
    private String bannerUrl;

    @Column(name = "stream_key", nullable = false, unique = true)
    @Builder.Default
    private UUID streamKey = UUID.randomUUID();

    @Column(name = "is_live", nullable = false)
    @Builder.Default
    private Boolean isLive = false;

    @Column(name = "viewer_count", nullable = false)
    @Builder.Default
    private Integer viewerCount = 0;

    @Column(length = 10)
    @Builder.Default
    private String language = "ar";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}