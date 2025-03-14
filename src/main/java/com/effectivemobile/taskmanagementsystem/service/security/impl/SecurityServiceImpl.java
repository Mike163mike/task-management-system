package com.effectivemobile.taskmanagementsystem.service.security.impl;

import com.effectivemobile.taskmanagementsystem.exception.CustomException;
import com.effectivemobile.taskmanagementsystem.security.JwtService;
import com.effectivemobile.taskmanagementsystem.service.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public Map<String, String> authenticateAndGenerateTokens(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,
                password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return Map.of(
                "accessToken", jwtService.generateAccessToken(userDetails),
                "refreshToken", jwtService.generateRefreshToken(userDetails)
        );
    }

    @Override
    public Map<String, String> refreshAccessToken(Map<String, String> response) {
        String refreshToken = response.get("refreshToken");

        if (refreshToken == null) {
            throw new CustomException("Refresh token is required", HttpStatus.BAD_REQUEST, this.getClass(),
                    "refreshAccessToken");
        }

        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.validateToken(refreshToken, userDetails)) {
            throw new CustomException("Invalid refresh token", HttpStatus.UNAUTHORIZED, this.getClass(),
                    "refreshAccessToken");
        }

        String newAccessToken = jwtService.generateAccessToken(userDetails);

        return Map.of("accessToken", newAccessToken);
    }
}
