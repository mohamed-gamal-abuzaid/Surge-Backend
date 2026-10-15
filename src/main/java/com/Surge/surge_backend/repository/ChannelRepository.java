package com.Surge.surge_backend.repository;

import com.Surge.surge_backend.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, UUID> {

    Optional<Channel> findByChannelName(String channelName);

    Optional<Channel> findByUserId(UUID userId);

    Boolean existsByChannelName(String channelName);
}