package com.Surge.surge_backend.controller;

import com.Surge.surge_backend.dtos.ApiResponse;
import com.Surge.surge_backend.dtos.Streaming.MediaMtxWebhookRequest;
import com.Surge.surge_backend.dtos.Streaming.StreamDto;
import com.Surge.surge_backend.service.StreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/internal/media")
@RequiredArgsConstructor
public class MediaWebhookController {

    private final StreamService streamService;

    // ========================================================
    // 1. Webhook التحقق وبدء البث (MediaMTX Authentication / Publish)
    // ========================================================
    @PostMapping("/publish")
    public ResponseEntity<ApiResponse> onStreamPublish(@RequestBody MediaMtxWebhookRequest request) {
        log.info("📡 Ingest Webhook received - Action: {}, Path: {}", request.getAction(), request.getPath());

        // إذا كان الطلب من مشاهد (Read / Playback) نسمح له فوراً مجاناً بدون فحص الـ stream key
        if ("read".equalsIgnoreCase(request.getAction())) {
            return ResponseEntity.ok(ApiResponse.success("Read access granted"));
        }

        try {
            // استخراج الـ StreamKey من الـ Path
            // الـ path بيكون على شكل: "live/550e8400-e29b-41d4-a716-446655440000" أو مجرد الـ UUID
            UUID streamKey = extractStreamKey(request.getPath());

            // بدء البث وتسجيله في الـ Database
            StreamDto streamDto = streamService.startStream(streamKey);
            log.info("✅ Stream authenticated and started successfully: {}", streamDto.getId());

            return ResponseEntity.ok(ApiResponse.success("Stream authorized"));
        } catch (Exception e) {
            log.error("❌ Stream rejected: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(401, "Unauthorized stream key: " + e.getMessage()));
        }
    }

    // ========================================================
    // 2. Webhook انتهاء البث (MediaMTX Unpublish)
    // ========================================================
    @PostMapping("/unpublish")
    public ResponseEntity<ApiResponse> onStreamUnpublish(@RequestBody MediaMtxWebhookRequest request) {
        log.info("🛑 Ingest Webhook (Unpublish) received for path: {}", request.getPath());

        try {
            UUID streamKey = extractStreamKey(request.getPath());
            streamService.stopStream(streamKey);
            return ResponseEntity.ok(ApiResponse.success("Stream ended successfully"));
        } catch (Exception e) {
            log.warn("Warning ending stream: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.success("Stream end acknowledged"));
        }
    }

    // Helper: فك الـ Stream Key من الـ Path
    private UUID extractStreamKey(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Path cannot be empty");
        }
        // لو الـ path بيبدأ بـ live/ نقصه
        String keyString = path.contains("/") ? path.substring(path.lastIndexOf('/') + 1) : path;
        return UUID.fromString(keyString.trim());
    }
}