package com.Surge.surge_backend.entity;



import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Entity
@Table(name = "streams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stream {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "ingest_url", length = 500)
    private String ingestUrl;

    @Column(name = "playback_url", length = 500)
    private String playbackUrl;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "LIVE";

    @Column(name = "peak_viewers", nullable = false)
    @Builder.Default
    private Integer peakViewers = 0;

    @Column(name = "avg_viewers", nullable = false)
    @Builder.Default
    private Integer avgViewers = 0;

    @Column(name = "started_at", nullable = false)
    @Builder.Default
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    // === Tags (Many-to-Many) ===
    @ManyToMany
    @JoinTable(
            name = "stream_tags",
            joinColumns = @JoinColumn(name = "stream_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();
}