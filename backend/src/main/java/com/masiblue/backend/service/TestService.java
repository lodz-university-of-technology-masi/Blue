package com.masiblue.backend.service;

import com.masiblue.backend.exception.*;
import com.masiblue.backend.model.*;
import com.masiblue.backend.repository.LanguageRepository;
import com.masiblue.backend.repository.TestRepository;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
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

    public List<Test> findAllByUsername(String name) throws UserAccountNotFoundException, ApplicationUserNotFoundException, AuthorizationException {
        ApplicationUser user = applicationUserService.findByUsername(name);
        if(user.getRole().getName().equals(RoleConstants.REDACTOR_ROLE)) {
            return testRepository.findAllByAuthor_Id(user.getId());
        } else if (user.getRole().getName().equals(RoleConstants.MODERATOR_ROLE)) {
            return testRepository.findAll();
        } else {
            throw new AuthorizationException();
        }
    }

    public Test findById(long id) throws TestNotFoundException {
        return testRepository.findById(id).orElseThrow(TestNotFoundException::new);
    }

    public List<Test> findAllForPositionId(long positionId, String username) throws UserAccountNotFoundException, ApplicationUserNotFoundException, AuthorizationException {
        ApplicationUser user = applicationUserService.findByUsername(username);

        switch (user.getRole().getName()) {
            case RoleConstants.REDACTOR_ROLE:
                return testRepository.findAllByPosition_IdAndAuthor_Id(positionId, user.getId());
            case RoleConstants.MODERATOR_ROLE:
                return testRepository.findAllByPosition_Id(positionId);
            case RoleConstants.USER_ROLE:
                //TODO: I think we should add field to test which will contain all users that are eligible for a given test and all users that already passed the test
                //TODO: Then based on this information we can limit tests sent to a user
                return testRepository.findAllByPosition_Id(positionId);
            default:
                throw new AuthorizationException();
        }
    }

    public List<Test> findAllForLanguageId(long languageId, String username) throws UserAccountNotFoundException, ApplicationUserNotFoundException, AuthorizationException {
        ApplicationUser user = applicationUserService.findByUsername(username);

        switch (user.getRole().getName()) {
            case RoleConstants.REDACTOR_ROLE:
                return testRepository.findAllByLanguage_IdAndAuthor_Id(languageId, user.getId());
            case RoleConstants.MODERATOR_ROLE:
                return testRepository.findAllByLanguage_Id(languageId);
            case RoleConstants.USER_ROLE:
                //TODO: I think we should add field to test which will contain all users that are eligible for a given test and all users that already passed the test
                //TODO: Then based on this infromation we can limit tests sent to a user
                return testRepository.findAllByLanguage_Id(languageId);
            default:
                throw new AuthorizationException();
        }
    }

    public List<Test> findAllByLangAndPos(long idlang, long idpos){
        return testRepository.findAllByLanguage_IdAndPosition_Id(idlang, idpos);
    }

    public void addNewTest(TestCreateDTO testInformation, String redactorName) throws LanguageNotFoundException, RedactorNotFoundException, ApplicationUserNotFoundException, PositionNotFoundException, UserAccountNotFoundException {
        Language language = languageService.findById(testInformation.getLanguageId());
        ApplicationUser redactor = applicationUserService.findRedactorByUsername(redactorName);
        Position position = positionService.findById(testInformation.getPositionId());

        Test newTest = new Test(testInformation.getName(), position, redactor, language);
        testRepository.save(newTest);
    }

    public void update(long id, Test newTest, String username) throws TestNotFoundException, PositionNotFoundException, LanguageNotFoundException, UserAccountNotFoundException, ApplicationUserNotFoundException, AuthorizationException, NotOwnerException {
        Test oldTest = findById(id);
        ApplicationUser user = applicationUserService.findByUsername(username);
        if(user.getRole().getName().equals(RoleConstants.REDACTOR_ROLE)) {
            if(oldTest.getId() != user.getId()) {
                throw new NotOwnerException();
            }
        } else if (!user.getRole().getName().equals(RoleConstants.MODERATOR_ROLE)) {
            throw new AuthorizationException();
        }

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
