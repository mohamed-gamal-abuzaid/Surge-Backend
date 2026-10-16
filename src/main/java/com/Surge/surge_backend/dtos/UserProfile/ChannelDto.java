package com.Surge.surge_backend.dtos.UserProfile;

import com.Surge.surge_backend.dtos.Auth.UserDto;
import lombok.*;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelDto {
    private UUID id;
    private String channelName;
    private String title;
    private String description;
    private String bannerUrl;
    private Boolean isLive;
    private Integer viewerCount;
    private String language;
    private UserDto user;
}