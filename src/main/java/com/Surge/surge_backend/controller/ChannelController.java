package com.Surge.surge_backend.controller;


import com.Surge.surge_backend.dtos.UserProfile.ChannelDto;
import com.Surge.surge_backend.dtos.UserProfile.StreamKeyResponse;
import com.Surge.surge_backend.dtos.UserProfile.UpdateChannelRequest;
import com.Surge.surge_backend.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    // === 1. استعراض صفحة القناة العامة ===
    @GetMapping("/{channelName}")
    public ResponseEntity<ChannelDto> getChannelByName(@PathVariable String channelName) {
        return ResponseEntity.ok(channelService.getChannelByName(channelName));
    }

    // === 2. تعديل إعدادات قناتي (عنوان البث، الوصف، البانر) ===
    @PutMapping("/me")
    public ResponseEntity<ChannelDto> updateMyChannel(
            @Valid @RequestBody UpdateChannelRequest request
    ) {
        UUID currentUserId = getCurrentUserId();
        return ResponseEntity.ok(channelService.updateMyChannel(currentUserId, request));
    }

    // === 3. جلب الـ Stream Key لقناتي ===
    @GetMapping("/me/stream-key")
    public ResponseEntity<StreamKeyResponse> getMyStreamKey() {
        UUID currentUserId = getCurrentUserId();
        return ResponseEntity.ok(channelService.getMyStreamKey(currentUserId));
    }

    // === 4. إعادة توليد مفتاح بث جديد ===
    @PostMapping("/me/stream-key/regenerate")
    public ResponseEntity<StreamKeyResponse> regenerateStreamKey() {
        UUID currentUserId = getCurrentUserId();
        return ResponseEntity.ok(channelService.regenerateStreamKey(currentUserId));
    }

    // Helper
    private UUID getCurrentUserId() {
        return (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}