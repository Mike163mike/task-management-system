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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public Map<String, String> authenticateAndGenerateTokens(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = getUserByEmail(email);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return generateAndSaveTokens(user, userDetails);
    }

    @Override
    @Transactional
    public Map<String, String> refreshAccessToken(Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null) {
            throw new CustomException("Refresh token is required", HttpStatus.BAD_REQUEST, this.getClass(),
                    "refreshAccessToken");
        }

        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new CustomException("Invalid refresh token", HttpStatus.UNAUTHORIZED, this.getClass(),
                        "refreshAccessToken"));

        if (storedToken.getExpiryDate().isBefore(OffsetDateTime.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new CustomException("Refresh token expired, please login again", HttpStatus.UNAUTHORIZED,
                    this.getClass(), "refreshAccessToken");
        }

        User user = getUserByEmail(storedToken.getUser().getEmail());
        return generateAndSaveTokens(user, new UserDetailsAdapter(user));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User %s not found" .formatted(email),
                        HttpStatus.NOT_FOUND, this.getClass(), "getUserByEmail"));
    }

    private Map<String, String> generateAndSaveTokens(User user, UserDetails userDetails) {
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.save(RefreshToken.builder()
                .token(refreshToken)
                .expiryDate(jwtService.getRefreshTokenExpiry())
                .user(user)
                .build());

        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }
}
