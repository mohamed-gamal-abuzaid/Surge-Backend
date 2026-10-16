package com.Surge.surge_backend.dtos.Streaming;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaMtxWebhookRequest {
    // اسم المسار أو الـ stream key اللي OBS بعته
    // في MediaMTX الـ path بيكون مثلاً: "live/550e8400-e29b-..."
    private String path;
    // نوع الإجراء (publish, read, unpublish)
    private String action;
    // عنوان IP الخاص بالستريمر
    private String ip;
    // باراميترات إضافية لو الـ URL فيه query params
    private String query;
}
