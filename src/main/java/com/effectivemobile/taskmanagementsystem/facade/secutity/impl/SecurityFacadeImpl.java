package com.effectivemobile.taskmanagementsystem.facade.secutity.impl;

import com.effectivemobile.taskmanagementsystem.dto.user.UserLoginDto;
import com.effectivemobile.taskmanagementsystem.facade.secutity.SecurityFacade;
import com.effectivemobile.taskmanagementsystem.service.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SecurityFacadeImpl implements SecurityFacade {

    private final SecurityService securityService;

    @Override
    public Map<String, String> login(UserLoginDto userLoginDto) {
        return securityService.authenticateAndGenerateTokens(userLoginDto.getEmail(), userLoginDto.getPassword());
    }

    @Override
    public Map<String, String> refreshAccessToken(Map<String, String> request) {
        return securityService.refreshAccessToken(request);
    }
}
