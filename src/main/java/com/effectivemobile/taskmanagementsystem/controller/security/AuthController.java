package com.effectivemobile.taskmanagementsystem.controller.security;

import com.effectivemobile.taskmanagementsystem.dto.user.UserLoginDto;
import com.effectivemobile.taskmanagementsystem.facade.secutity.SecurityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.APP_PREFIX;

@RestController
@RequestMapping(APP_PREFIX + "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SecurityFacade securityFacade;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginDto userLoginDto) {
        return ResponseEntity.ok(securityFacade.login(userLoginDto));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(securityFacade.refreshAccessToken(request));
    }
}
