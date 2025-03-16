package com.effectivemobile.taskmanagementsystem.service.security.impl;

import com.effectivemobile.taskmanagementsystem.entity.security.RefreshToken;
import com.effectivemobile.taskmanagementsystem.entity.user.User;
import com.effectivemobile.taskmanagementsystem.exception.CustomException;
import com.effectivemobile.taskmanagementsystem.repository.security.RefreshTokenRepository;
import com.effectivemobile.taskmanagementsystem.repository.user.UserRepository;
import com.effectivemobile.taskmanagementsystem.security.JwtService;
import com.effectivemobile.taskmanagementsystem.security.UserDetailsAdapter;
import com.effectivemobile.taskmanagementsystem.service.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityServiceImpl implements SecurityService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public Map<String, String> authenticateAndGenerateTokens(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,
                password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User %s not found" .formatted(email), HttpStatus.NOT_FOUND,
                        this.getClass(), "authenticateAndGenerateTokens"));

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        RefreshToken savedRefreshToken = RefreshToken.builder()
                .token(refreshToken)
                .expiryDate(jwtService.getRefreshTokenExpiry())
                .user(user)
                .build();

        if (refreshTokenRepository.existsByUser(user)) {
            refreshTokenRepository.deleteByUser(user);
        }

        refreshTokenRepository.save(savedRefreshToken);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    @Override
    @Transactional
    public Map<String, String> refreshAccessToken(Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null) {
            throw new CustomException("Refresh token is required", HttpStatus.BAD_REQUEST, this.getClass(),
                    "refreshAccessToken");
        }

        List<RefreshToken> tokens = refreshTokenRepository.findAll();
        log.info("Stored tokens in DB: ============>>>>>>>>>>{}", tokens);
        log.info("Trying to find refresh token: ==========={}", refreshToken);


        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new CustomException("Invalid refresh token", HttpStatus.UNAUTHORIZED, this.getClass(),
                        "refreshAccessToken"));

        if (storedToken.getExpiryDate().isBefore(OffsetDateTime.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new CustomException("Refresh token expired, please login again", HttpStatus.UNAUTHORIZED,
                    this.getClass(), "refreshAccessToken");
        }

        User user = storedToken.getUser();

        User savedUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new CustomException("User %s not found" .formatted(user.getEmail()),
                        HttpStatus.NOT_FOUND, this.getClass(), "refreshAccessToken"));

        String newAccessToken = jwtService.generateAccessToken(new UserDetailsAdapter(savedUser));


        if (storedToken.getExpiryDate().isAfter(OffsetDateTime.now())) {
            return Map.of(
                    "accessToken", newAccessToken,
                    "refreshToken", refreshToken
            );
        }

        String newRefreshToken = jwtService.generateRefreshToken(new UserDetailsAdapter(savedUser));

        if (refreshTokenRepository.existsByToken(newRefreshToken)) {
            throw new CustomException("Token already exists", HttpStatus.BAD_REQUEST, this.getClass(),
                    "refreshAccessToken");
        }

        refreshTokenRepository.delete(storedToken);

//        refreshTokenRepository.deleteByToken(refreshToken);

        RefreshToken newRefreshTokenEntity = RefreshToken.builder()
                .token(newRefreshToken)
                .expiryDate(jwtService.getRefreshTokenExpiry())
                .user(savedUser)
                .build();

        refreshTokenRepository.save(newRefreshTokenEntity);

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }
}
