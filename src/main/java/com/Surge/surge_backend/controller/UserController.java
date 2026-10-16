package com.Surge.surge_backend.controller;


import com.Surge.surge_backend.dtos.Auth.UserDto;
import com.Surge.surge_backend.dtos.UserProfile.UpdateUserProfileRequest;
import com.Surge.surge_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // === 1. جلب بيانات حسابي الحالي ===
    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile() {
        UUID currentUserId = getCurrentUserId();
        return ResponseEntity.ok(userService.getMyProfile(currentUserId));
    }

    // === 2. تعديل بيانات حسابي ===
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMyProfile(
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        UUID currentUserId = getCurrentUserId();
        return ResponseEntity.ok(userService.updateMyProfile(currentUserId, request));
    }

    // === 3. استعراض بروفايل عام لأي مستخدم ===
    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    // Helper لاستخراج الـ userId من الـ Security Context (اللي تم استخراجه من الـ JWT)
    private UUID getCurrentUserId() {
        return (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}