package com.Surge.surge_backend.dtos.UserProfile;

import jakarta.validation.constraints.Size;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateChannelRequest {
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;
    @Size(max = 500, message = "Banner URL cannot exceed 500 characters")
    private String bannerUrl;
    @Size(max = 10, message = "Language code cannot exceed 10 characters")
    private String language;
}