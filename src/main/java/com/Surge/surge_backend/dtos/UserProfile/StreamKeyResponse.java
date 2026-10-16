package com.Surge.surge_backend.dtos.UserProfile;

import lombok.*;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StreamKeyResponse {
    private UUID streamKey;
    private String ingestServerUrl; // مثلاً rtmp://localhost:1935/live
}