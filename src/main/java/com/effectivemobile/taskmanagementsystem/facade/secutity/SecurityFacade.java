package com.effectivemobile.taskmanagementsystem.facade.secutity;

import com.effectivemobile.taskmanagementsystem.dto.user.UserLoginDto;

import java.util.Map;

public interface SecurityFacade {

    Map<String, String> login(UserLoginDto userLoginDto);
}
