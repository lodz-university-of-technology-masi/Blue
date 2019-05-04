package com.masiblue.backend.service;

import com.masiblue.backend.exception.*;
import com.masiblue.backend.model.*;
import com.masiblue.backend.repository.LanguageRepository;
import com.masiblue.backend.repository.TestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    private final TestRepository testRepository;
    private final ApplicationUserService applicationUserService;
    private final LanguageRepository languageRepository;
    private final PositionService positionService;

    public TestService(TestRepository testRepository, ApplicationUserService applicationUserService, LanguageRepository languageRepository, PositionService positionService) {
        this.testRepository = testRepository;
        this.applicationUserService = applicationUserService;
        this.languageRepository = languageRepository;
        this.positionService = positionService;
    }

    public List<Test> findAll() {
        return testRepository.findAll();
    }

    public Test findById(long id) throws TestNotFoundException {
        return testRepository.findById(id).orElseThrow(TestNotFoundException::new);
    }

    public List<Test> findAllForPositionId(long id) {
        return testRepository.findAllByPosition_Id(id);
    }

    public List<Test> findAllForLanguageId(long id) {
        return testRepository.findAllByLanguage_Id(id);
    }

    public List<Test> findAllForModeratorId(long id) {
        return testRepository.findAllByAuthor_Id(id);
    }

    public void addNewTest(TestCreateDTO testInformation) throws LanguageNotFoundException, RedactorNotFoundException, ApplicationUserNotFoundException, PositionNotFoundException {
        Language language = languageRepository.findById(testInformation.getLanguageId()).orElseThrow(LanguageNotFoundException::new);
        ApplicationUser redactor = applicationUserService.findRedactorById(testInformation.getAuthorId());
        Position position = positionService.findById(testInformation.getPositionId());

        Test newTest = new Test(testInformation.getName(), position, redactor, language);
        testRepository.save(newTest);
    }
}
