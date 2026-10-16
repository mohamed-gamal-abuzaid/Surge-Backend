package com.Surge.surge_backend.service;


import com.Surge.surge_backend.dtos.UserProfile.ChannelDto;
import com.Surge.surge_backend.dtos.UserProfile.StreamKeyResponse;
import com.Surge.surge_backend.dtos.UserProfile.UpdateChannelRequest;
import com.Surge.surge_backend.entity.Channel;
import com.Surge.surge_backend.exception.ResourceNotFoundException;
import com.Surge.surge_backend.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;

    // رابط سيرفر الـ RTMP الافتراضي
    private static final String DEFAULT_INGEST_URL = "rtmp://localhost:1935/live";

    // === جلب بيانات القناة العامة باسم القناة ===
    public ChannelDto getChannelByName(String channelName) {
        Channel channel = channelRepository.findByChannelName(channelName)
                .orElseThrow(() -> new ResourceNotFoundException("Channel not found: " + channelName));
        return mapToChannelDto(channel);
    }

    // === تعديل بيانات قناتي (عنوان البث، الوصف، البانر) ===
    @Transactional
    public ChannelDto updateMyChannel(UUID currentUserId, UpdateChannelRequest request) {
        Channel channel = getChannelByUserId(currentUserId);

        if (request.getTitle() != null) {
            channel.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            channel.setDescription(request.getDescription());
        }
        if (request.getBannerUrl() != null) {
            channel.setBannerUrl(request.getBannerUrl());
        }
        if (request.getLanguage() != null) {
            channel.setLanguage(request.getLanguage());
        }

        channel = channelRepository.save(channel);
        return mapToChannelDto(channel);
    }

    // === جلب الـ Stream Key لقناتي فقط ===
    public StreamKeyResponse getMyStreamKey(UUID currentUserId) {
        Channel channel = getChannelByUserId(currentUserId);

        return StreamKeyResponse.builder()
                .streamKey(channel.getStreamKey())
                .ingestServerUrl(DEFAULT_INGEST_URL)
                .build();
    }

    // === إعادة توليد Stream Key جديد ===
    @Transactional
    public StreamKeyResponse regenerateStreamKey(UUID currentUserId) {
        Channel channel = getChannelByUserId(currentUserId);

        // توليد مفتاح عشوائي جديد تماماً
        channel.setStreamKey(UUID.randomUUID());
        channelRepository.save(channel);

        return StreamKeyResponse.builder()
                .streamKey(channel.getStreamKey())
                .ingestServerUrl(DEFAULT_INGEST_URL)
                .build();
    }

    // Helper: جلب قناة اليوزر
    private Channel getChannelByUserId(UUID userId) {
        return channelRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel not found for current user"));
    }

    // Helper: تحويل Entity إلى DTO
    public ChannelDto mapToChannelDto(Channel channel) {
        return ChannelDto.builder()
                .id(channel.getId())
                .channelName(channel.getChannelName())
                .title(channel.getTitle())
                .description(channel.getDescription())
                .bannerUrl(channel.getBannerUrl())
                .isLive(channel.getIsLive())
                .viewerCount(channel.getViewerCount())
                .language(channel.getLanguage())
                .user(userService.mapToUserDto(channel.getUser()))
                .build();
    }
}