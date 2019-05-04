package com.masiblue.backend.controller;

import com.masiblue.backend.service.LanguageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MODERATOR', 'REDACTOR', 'USER')")
    public List listAllLanguages() {
        return languageService.findAll();
    }

}
