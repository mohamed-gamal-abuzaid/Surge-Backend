package com.Surge.surge_backend.controller;

import com.Surge.surge_backend.dtos.Streaming.StreamDto;
import com.Surge.surge_backend.service.StreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/streams")
@RequiredArgsConstructor
public class StreamController {

    private final StreamService streamService;

    // === 1. جلب كل البثوث المباشرة الحالية (الصفحة الرئيسية) ===
    @GetMapping
    public ResponseEntity<List<StreamDto>> getAllLiveStreams() {
        return ResponseEntity.ok(streamService.getAllLiveStreams());
    }

    // === 2. جلب بيانات بث معين بالـ ID ===
    @GetMapping("/{streamId}")
    public ResponseEntity<StreamDto> getStreamById(@PathVariable UUID streamId) {
        return ResponseEntity.ok(streamService.getStreamById(streamId));
    }
}