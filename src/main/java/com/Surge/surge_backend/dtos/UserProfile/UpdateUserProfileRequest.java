package com.Surge.surge_backend.dtos.UserProfile;

import jakarta.validation.constraints.Size;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserProfileRequest {
    @Size(max = 100, message = "Display name cannot exceed 100 characters")
    private String displayName;
    @Size(max = 500, message = "Avatar URL cannot exceed 500 characters")
    private String avatarUrl;
    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;
}