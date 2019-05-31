package com.masiblue.backend.service;

import com.masiblue.backend.exception.LanguageAlreadExistsException;
import com.masiblue.backend.exception.LanguageNotFoundException;
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

    public boolean exists(Language language) throws LanguageNotFoundException {
        return languageRepository.findById(language.getId()).orElseThrow(LanguageNotFoundException::new).equals(language);
    }

    public Language findById(long languageId) throws LanguageNotFoundException {
        return languageRepository.findById(languageId).orElseThrow(LanguageNotFoundException::new);
    }

    public Language findByName(String languageName) throws LanguageNotFoundException {
        return languageRepository.findByName(languageName).orElseThrow(LanguageNotFoundException::new);
    }
}
