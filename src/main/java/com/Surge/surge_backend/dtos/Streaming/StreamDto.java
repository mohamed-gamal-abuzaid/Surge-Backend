package com.Surge.surge_backend.dtos.Streaming;

import com.Surge.surge_backend.dtos.UserProfile.ChannelDto;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StreamDto {
    private UUID id;
    private String title;
    private String thumbnailUrl;
    private String playbackUrl; // رابط المشاهدة (HLS/WebRTC)
    private String status;      // LIVE أو ENDED
    private Integer viewerCount;
    private LocalDateTime startedAt;
    private ChannelDto channel;
}