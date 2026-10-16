package com.Surge.surge_backend.repository;

import com.Surge.surge_backend.entity.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StreamRepository extends JpaRepository<Stream, UUID> {

    // جلب كل البثوث الحالية المباشرة مرتبة بالأحدث
    List<Stream> findAllByStatusOrderByStartedAtDesc(String status);

    // البحث عن البث المباشر الحالي لقناة معينة
    Optional<Stream> findByChannelIdAndStatus(UUID channelId, String status);

    // البحث عن آخر بث للقناة
    Optional<Stream> findFirstByChannelIdOrderByStartedAtDesc(UUID channelId);
}