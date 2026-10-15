package com.Surge.surge_backend.service;

import com.Surge.surge_backend.dtos.Auth.*;
import com.Surge.surge_backend.entity.Channel;
import com.Surge.surge_backend.entity.User;
import com.Surge.surge_backend.exception.*;
import com.Surge.surge_backend.repository.ChannelRepository;
import com.Surge.surge_backend.repository.UserRepository;
import com.Surge.surge_backend.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // ===========================
    //         REGISTER
    // ===========================
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // 1. تشيك إن الـ username مش متاخد
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException(
                    "Username '" + request.getUsername() + "' is already taken"
            );
        }

        // 2. تشيك إن الـ email مش متاخد
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email '" + request.getEmail() + "' is already registered"
            );
        }

        // 3. اعمل الـ User entity
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .displayName(request.getDisplayName() != null
                        ? request.getDisplayName()
                        : request.getUsername())
                .role("USER")
                .build();

        // 4. احفظ الـ User في الـ DB
        user = userRepository.save(user);

        // 5. اعمل Channel تلقائي لليوزر الجديد
        Channel channel = Channel.builder()
                .user(user)
                .channelName(user.getUsername())
                .title(user.getDisplayName() + "'s channel")
                .build();

        channelRepository.save(channel);

        // 6. ولّد الـ tokens وارجع الـ response
        return buildAuthResponse(user);
    }

    // ===========================
    //          LOGIN
    // ===========================
    public AuthResponse login(LoginRequest request) {

        // 1. دور على اليوزر بالـ username أو email
        User user = userRepository
                .findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username/email or password"));

        // 2. قارن الباسورد
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid username/email or password");
        }

        // 3. شيك إن اليوزر مش مبان
        if (user.getIsBanned()) {
            throw new InvalidCredentialsException("This account has been banned");
        }

        // 4. ولّد الـ tokens وارجع الـ response
        return buildAuthResponse(user);
    }

    // ===========================
    //      REFRESH TOKEN
    // ===========================
    public AuthResponse refreshToken(String refreshToken) {

        // 1. تحقق إن الـ token صالح
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        // 2. طلع الـ userId من الـ token
        UUID userId = jwtProvider.getUserIdFromToken(refreshToken);

        // 3. جيب اليوزر من الـ DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidTokenException("User not found"));

        // 4. ولّد tokens جديدة
        return buildAuthResponse(user);
    }

    // ===========================
    //     CHANGE PASSWORD
    // ===========================
    public void changePassword(UUID userId, ChangePasswordRequest request) {

        // 1. تأكد إن الباسورد الجديد والتأكيد متطابقين
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidCredentialsException("New password and confirm password do not match");
        }

        // 2. جيب اليوزر من الـ DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        // 3. تحقق إن الباسورد الحالي صح
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        // 4. تأكد إن الباسورد الجديد مختلف عن القديم
        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("New password must be different from current password");
        }

        // 5. شفّر الباسورد الجديد واحفظه
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // ===========================
    //    HELPER: Build Response
    // ===========================
    private AuthResponse buildAuthResponse(User user) {

        String accessToken = jwtProvider.generateAccessToken(
                user.getId(), user.getUsername(), user.getRole()
        );

        String refreshToken = jwtProvider.generateRefreshToken(
                user.getId(), user.getUsername(), user.getRole()
        );

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .build();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtProvider.getAccessExpiration())
                .user(userDto)
                .build();
    }
}