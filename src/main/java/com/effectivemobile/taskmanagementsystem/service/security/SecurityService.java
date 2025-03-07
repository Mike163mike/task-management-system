package com.effectivemobile.taskmanagementsystem.service.security;

import java.util.Map;

public interface SecurityService {

    Map<String, String> login(String email, String password);
}
