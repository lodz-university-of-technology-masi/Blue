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
    private final LanguageService languageService;
    private final PositionService positionService;

    public TestService(TestRepository testRepository, ApplicationUserService applicationUserService, LanguageService languageService, PositionService positionService) {
        this.testRepository = testRepository;
        this.applicationUserService = applicationUserService;
        this.languageService = languageService;
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

    public List<Test> findAllByLangAndPos(TestSolveDTO testInformation){
        return testRepository.findAllByLanguage_IdAndPosition_Id(testInformation.getLanguageId(), testInformation.getPositionId());
    }

    public void addNewTest(TestCreateDTO testInformation) throws LanguageNotFoundException, RedactorNotFoundException, ApplicationUserNotFoundException, PositionNotFoundException {
        Language language = languageService.findById(testInformation.getLanguageId());
        ApplicationUser redactor = applicationUserService.findRedactorById(testInformation.getAuthorId());
        Position position = positionService.findById(testInformation.getPositionId());

        Test newTest = new Test(testInformation.getName(), position, redactor, language);
        testRepository.save(newTest);
    }

    public void update(long id, Test newTest) throws TestNotFoundException, PositionNotFoundException, LanguageNotFoundException {
        Test oldTest = findById(id);
        validateNewTestData(oldTest, newTest);
        testRepository.save(newTest);
    }

    private void validateNewTestData(Test oldTest, Test newTest) throws PositionNotFoundException, LanguageNotFoundException {
        newTest.setId(oldTest.getId());
        if(newTest.getName() == null) {
            newTest.setName(oldTest.getName());
        }
        newTest.setAuthor(oldTest.getAuthor());
        if(newTest.getPosition() == null) {
            newTest.setPosition(oldTest.getPosition());
        } else if (!positionService.exists(newTest.getPosition())) {
            throw new PositionNotFoundException();
        }
        if(newTest.getLanguage() == null) {
            newTest.setLanguage(oldTest.getLanguage());
        } else if (!languageService.exists(newTest.getLanguage())) {
            throw new LanguageNotFoundException();
        }
        newTest.setCreationDate(oldTest.getCreationDate());
        newTest.setModificationDate(oldTest.getModificationDate());
    }
}
