package com.masiblue.backend.service;

import com.masiblue.backend.exception.*;
import com.masiblue.backend.model.*;
import com.masiblue.backend.repository.TestRepository;
import com.masiblue.backend.translation.GoogleTranslator;
import com.masiblue.backend.translation.Translator;
import org.springframework.stereotype.Service;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

@Service
public class TestService {

    private final TestRepository testRepository;
    private final ApplicationUserService applicationUserService;
    private final LanguageService languageService;
    private final PositionService positionService;
    private final CsvService csvService;

    public TestService(TestRepository testRepository, ApplicationUserService applicationUserService, LanguageService languageService, PositionService positionService, CsvService csvService) {
        this.testRepository = testRepository;
        this.applicationUserService = applicationUserService;
        this.languageService = languageService;
        this.positionService = positionService;
        this.csvService = csvService;
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

    public void importTestFromCsv(TestCreateDTO testCreateDTO, String authorUsername, MultipartFile file)
            throws ApplicationUserNotFoundException, InvalidCsvException, PositionNotFoundException, UserAccountNotFoundException {

        Test test = new Test();
        test.setName(testCreateDTO.getName());
        test.setCreationDate(LocalDateTime.now());

        Position position = positionService.findById(testCreateDTO.getPositionId());
        test.setPosition(position);

        List<CsvQuestionModel> csvQuestions = csvService.readCsvQuestions(file);
        if (csvQuestions.isEmpty()) {
            throw new InvalidCsvException();
        }
        List<Question> questions = parseToQuestionModel(csvQuestions, test);
        test.setQuestions(questions);

        ApplicationUser author = applicationUserService.findByUsername(authorUsername);
        test.setAuthor(author);

        // assuming that all questions in given test are in the same language
        String languageFromCsv = csvQuestions.get(0).getLanguage();
        try {
            Language language = findOrCreateLanguage(languageFromCsv);
            test.setLanguage(language);
        } catch (LanguageAlreadExistsException | LanguageNotFoundException e) {
            throw new InvalidCsvException();
        }

        testRepository.save(test);
    }

    private Language findOrCreateLanguage(String languageFromCsv) throws LanguageAlreadExistsException, LanguageNotFoundException {
        try {
            return languageService.findByName(languageFromCsv);
        } catch (LanguageNotFoundException e) {
            languageService.add(languageFromCsv);
            return languageService.findByName(languageFromCsv);
        }
    }

    private List<Question> parseToQuestionModel(List<CsvQuestionModel> csvQuestions, Test test) {
        List<Question> questions = new ArrayList<>();
        csvQuestions.forEach(q -> {
            Question question = new Question();
            question.setType(Enum.valueOf(Type.class, q.getType()));
            question.setContent(q.getContent());
            String[] splitPossibleAnswers = q.getPossibleAnswers().split("\\|");
            question.setPossibleAnswers(new HashSet<>(Arrays.asList(splitPossibleAnswers)));
            question.setTest(test);
            questions.add(question);
        });
        return questions;
    }

    public void exportTestToCsv(long testId, HttpServletResponse response) throws CsvRequiredFieldEmptyException,
            IOException, CsvDataTypeMismatchException, TestNotFoundException {
        Test test = findById(testId);
        csvService.exportToCsvFile(test, response);
    }

    public void update(long id, TestCreateDTO newTest, String username) throws TestNotFoundException, PositionNotFoundException, LanguageNotFoundException, UserAccountNotFoundException, ApplicationUserNotFoundException, AuthorizationException, NotOwnerException {
        Test oldTest = findById(id);
        ApplicationUser user = applicationUserService.findByUsername(username);
        if(user.getRole().getName().equals(RoleConstants.REDACTOR_ROLE)) {
            if(oldTest.getAuthor().getId() != user.getId()) {
                throw new NotOwnerException();
            }
        } else if (!user.getRole().getName().equals(RoleConstants.MODERATOR_ROLE)) {
            throw new AuthorizationException();
        }

        updateTestData(oldTest, newTest);
        testRepository.save(oldTest);
    }

    private void updateTestData(Test oldTest, TestCreateDTO newTestData) throws PositionNotFoundException, LanguageNotFoundException {
        if(newTestData.getName() != null) {
            oldTest.setName(newTestData.getName());
        }
        if(newTestData.getPositionId() != oldTest.getPosition().getId()) {
            Position newPosition = positionService.findById(newTestData.getPositionId());
            oldTest.setPosition(newPosition);
        }
        if(newTestData.getLanguageId() != oldTest.getLanguage().getId()) {
            Language newLanguage = languageService.findById(newTestData.getLanguageId());
            oldTest.setLanguage(newLanguage);
        }
        oldTest.setCreationDate(oldTest.getCreationDate());
        oldTest.setModificationDate(LocalDateTime.now());
    }

    public List getQuestionsForId(long testId, String username) throws TestNotFoundException, UserAccountNotFoundException, ApplicationUserNotFoundException, NotOwnerException {
        Test test = findById(testId);
        ApplicationUser user = applicationUserService.findByUsername(username);
        if(test.getAuthor().getId() != user.getId()) {
            throw new NotOwnerException();
        }
        return test.getQuestions();
    }

    public void deleteTest(long testId, String username) throws TestNotFoundException, UserAccountNotFoundException, ApplicationUserNotFoundException, NotOwnerException {
        Test test = findById(testId);
        ApplicationUser user = applicationUserService.findByUsername(username);
        if(test.getAuthor().getId() != user.getId()) {
            throw new NotOwnerException();
        }
        testRepository.delete(test);
    }

    public void translateTest(TranslateTestDTO translateTestDTO, String username) throws TestNotFoundException, UserAccountNotFoundException, ApplicationUserNotFoundException, NotOwnerException, LanguageNotFoundException, LanguageTranslationNotSupportedException, LanguageAlreadExistsException {
        Test oldTest = findById(translateTestDTO.getId());
        ApplicationUser user = applicationUserService.findByUsername(username);
        Language newLanguage = languageService.findById(translateTestDTO.getLanguageId());
        if(oldTest.getAuthor().getId() != user.getId()) {
            throw new NotOwnerException();
        }
        if(oldTest.getLanguage() == newLanguage) {
            throw new LanguageAlreadExistsException();
        }
        Translator translator = new GoogleTranslator();
        Test newTest = translator.translateTest(oldTest, newLanguage);
        newTest.setName(translateTestDTO.getTestName());
        testRepository.save(newTest);
    }
}
