package com.Surge.surge_backend.service;


import com.Surge.surge_backend.dtos.Auth.UserDto;
import com.Surge.surge_backend.dtos.UserProfile.UpdateUserProfileRequest;
import com.Surge.surge_backend.entity.User;
import com.Surge.surge_backend.exception.ResourceNotFoundException;
import com.Surge.surge_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // === جلب بروفايلي الحالي ===
    public UserDto getMyProfile(UUID currentUserId) {
        User user = findUserById(currentUserId);
        return mapToUserDto(user);
    }

    // === جلب بروفايل أي يوزر بالـ username ===
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return mapToUserDto(user);
    }

    // === تعديل بروفايلي الحالي ===
    @Transactional
    public UserDto updateMyProfile(UUID currentUserId, UpdateUserProfileRequest request) {
        User user = findUserById(currentUserId);

        if (request.getDisplayName() != null && !request.getDisplayName().isBlank()) {
            user.setDisplayName(request.getDisplayName());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        user = userRepository.save(user);
        return mapToUserDto(user);
    }

    // Helper: البحث بالـ ID
    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    // Helper: تحويل Entity إلى DTO
    public UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .build();
    }
}