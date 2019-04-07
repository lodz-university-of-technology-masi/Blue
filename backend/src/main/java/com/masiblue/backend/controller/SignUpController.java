package com.masiblue.backend.controller;


import com.auth0.jwt.JWT;
import com.masiblue.backend.model.ApplicationUser;
import com.masiblue.backend.service.ApplicationUserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.masiblue.backend.security.SecurityConstants.*;

@RestController
@RequestMapping
public class SignUpController {

    private final ApplicationUserService applicationUserService;

    public SignUpController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody ApplicationUser user) {
        applicationUserService.save(user);
    }
}
