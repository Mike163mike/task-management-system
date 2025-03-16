package com.effectivemobile.taskmanagementsystem.service.security;

import java.util.Map;

public interface SecurityService {

    Map<String, String> authenticateAndGenerateTokens(String email, String password);

    Map<String, String> refreshAccessToken(Map<String, String> request);
}
