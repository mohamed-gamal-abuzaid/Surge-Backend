package com.Surge.surge_backend.dtos.Streaming;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartStreamManualRequest {
    @NotNull(message = "Stream key is required")
    private UUID streamKey;
    private String title;
}