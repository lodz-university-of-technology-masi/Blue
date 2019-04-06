package com.masiblue.backend.controller;


import com.masiblue.backend.model.ApplicationUser;
import com.masiblue.backend.model.ApplicationUserDTO;
import com.masiblue.backend.repository.ApplicationUserRepository;
import com.masiblue.backend.service.ApplicationUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class ApplicationUserController {

    private ApplicationUserService applicationUserService;

    public ApplicationUserController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List listUers() {
        return applicationUserService.findAll().stream().map(ApplicationUserDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody ApplicationUser user) {
        applicationUserService.save(user);
    }

}
