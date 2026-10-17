package com.Surge.surge_backend.service;

import com.Surge.surge_backend.dtos.Streaming.StreamDto;
import com.Surge.surge_backend.entity.Channel;
import com.Surge.surge_backend.entity.Stream;
import com.Surge.surge_backend.exception.ResourceNotFoundException;
import com.Surge.surge_backend.repository.ChannelRepository;
import com.Surge.surge_backend.repository.StreamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamService {

    private final StreamRepository streamRepository;
    private final ChannelRepository channelRepository;
    private final ChannelService channelService;

    // رابط تشغيل البث المباشر بتقنية HLS من سيرفر MediaMTX
    // المشاهد بيفتح الرابط ده في المتصفح أو مشغل الفيديو (Hls.js / Video.js)
    private static final String HLS_BASE_URL = "http://localhost:8888";

    // ========================================================
    // 1. بدء البث (يتم استدعاؤها لما OBS يبدأ يبعت داتا)
    // ========================================================
    @Transactional
    public StreamDto startStream(UUID streamKey) {
        // 1. التأكد من صحة مفتاح البث
        Channel channel = channelRepository.findByStreamKey(streamKey)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Stream Key: " + streamKey));

        // 2. لو في بث قديم لسه مفتوح بالغلط، نقفله الأول
        streamRepository.findByChannelIdAndStatus(channel.getId(), "LIVE")
                .ifPresent(existingStream -> {
                    existingStream.setStatus("ENDED");
                    existingStream.setEndedAt(LocalDateTime.now());
                    streamRepository.save(existingStream);
                });

        // 3. تحديث حالة القناة لتصبح Live
        channel.setIsLive(true);
        channelRepository.save(channel);

        // 4. تجهيز روابط البث المطابقة لمسار MediaMTX
        String playbackUrl = HLS_BASE_URL + "/live/" + streamKey + "/index.m3u8";

        // 5. إنشاء سجل البث المباشر
        Stream stream = Stream.builder()
                .channel(channel)
                .title(channel.getTitle() != null ? channel.getTitle() : channel.getChannelName() + " is live!")
                .playbackUrl(playbackUrl)
                .ingestUrl("rtmp://localhost:1935/live")
                .status("LIVE")
                .startedAt(LocalDateTime.now())
                .peakViewers(0)
                .avgViewers(0)
                .build();

        stream = streamRepository.save(stream);
        log.info("🎬 Channel '{}' went LIVE with Stream ID: {}", channel.getChannelName(), stream.getId());

        return mapToStreamDto(stream);
    }

    // ========================================================
    // 2. إنهاء البث (يتم استدعاؤها لما OBS يقفل)
    // ========================================================
    @Transactional
    public void stopStream(UUID streamKey) {
        Channel channel = channelRepository.findByStreamKey(streamKey)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Stream Key: " + streamKey));

        // 1. تحديث القناة
        channel.setIsLive(false);
        channel.setViewerCount(0);
        channelRepository.save(channel);

        // 2. تحديث سجل البث
        streamRepository.findByChannelIdAndStatus(channel.getId(), "LIVE")
                .ifPresent(stream -> {
                    stream.setStatus("ENDED");
                    stream.setEndedAt(LocalDateTime.now());
                    streamRepository.save(stream);
                    log.info("🛑 Channel '{}' STOPPED streaming. Stream ID: {}", channel.getChannelName(), stream.getId());
                });
    }

    // ========================================================
    // 3. جلب كل البثوث المباشرة للجمهور (الصفحة الرئيسية)
    // ========================================================
    public List<StreamDto> getAllLiveStreams() {
        return streamRepository.findAllByStatusOrderByStartedAtDesc("LIVE")
                .stream()
                .map(this::mapToStreamDto)
                .collect(Collectors.toList());
    }

    // ========================================================
    // 4. جلب تفاصيل بث معين
    // ========================================================
    public StreamDto getStreamById(UUID streamId) {
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new ResourceNotFoundException("Stream not found: " + streamId));
        return mapToStreamDto(stream);
    }

    // Helper: تحويل Entity إلى DTO
    public StreamDto mapToStreamDto(Stream stream) {
        return StreamDto.builder()
                .id(stream.getId())
                .title(stream.getTitle())
                .thumbnailUrl(stream.getThumbnailUrl())
                .playbackUrl(stream.getPlaybackUrl())
                .status(stream.getStatus())
                .viewerCount(stream.getChannel().getViewerCount())
                .startedAt(stream.getStartedAt())
                .channel(channelService.mapToChannelDto(stream.getChannel()))
                .build();
    }
}