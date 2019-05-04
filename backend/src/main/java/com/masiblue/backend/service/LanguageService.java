package com.masiblue.backend.service;

import com.masiblue.backend.exception.LanguageAlreadExistsException;
import com.masiblue.backend.model.Language;
import com.masiblue.backend.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public void add(String name) throws LanguageAlreadExistsException {
        if(this.languageRepository.findByName(name).isPresent()) {
            throw new LanguageAlreadExistsException();
        }
        this.languageRepository.save(new Language(name));
    }

    public void add(Language language) throws LanguageAlreadExistsException {
        if(this.languageRepository.findById(language.getId()).isPresent()
            || this.languageRepository.findByName(language.getName()).isPresent()) {
            throw new LanguageAlreadExistsException();
        }
        this.languageRepository.save(language);
    }

    public List findAll() {
        return languageRepository.findAll();
    }
}
