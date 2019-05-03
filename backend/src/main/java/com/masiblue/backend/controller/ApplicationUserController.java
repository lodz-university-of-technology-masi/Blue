package com.masiblue.backend.controller;

import com.masiblue.backend.service.ApplicationUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;

    public ApplicationUserController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping
    public List listUsers() {
        return applicationUserService.findAll();
    }
}
