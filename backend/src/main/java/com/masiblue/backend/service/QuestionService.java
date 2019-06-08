package com.masiblue.backend.service;

import com.masiblue.backend.exception.*;
import com.masiblue.backend.model.*;
import com.masiblue.backend.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final ApplicationUserService applicationUserService;
    private final LanguageService languageService;
    private final TestService testService;

    public QuestionService(QuestionRepository questionRepository, ApplicationUserService applicationUserService, LanguageService languageService, TestService testService) {
        this.questionRepository = questionRepository;
        this.applicationUserService = applicationUserService;
        this.languageService = languageService;
        this.testService = testService;
    }

    public Question findById(long id) throws QuestionNotFoundException {
        return questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
    }

    public boolean addNewQuestion(QuestionCreateDTO questionDTO, String name) throws UserAccountNotFoundException, ApplicationUserNotFoundException, NotOwnerException, QuestionTypeNotFoundException, AnswerListEmptyException, EmptyQuestionContentException, TestNotFoundException {
        ApplicationUser user = applicationUserService.findByUsername(name);
        Test questionTest = testService.findById(questionDTO.getTestId());
        if(questionTest.getAuthor().getId() != user.getId()) {
            throw new NotOwnerException();
        }
        Question questionToSave = createFromDto(questionDTO);
        questionRepository.save(questionToSave);
        return true;
    }

    public void updateQuestion(QuestionCreateDTO questionDTO, long id, String name) throws QuestionNotFoundException, NotOwnerException, QuestionTypeNotFoundException, AnswerListEmptyException, EmptyQuestionContentException, UserAccountNotFoundException, ApplicationUserNotFoundException, TestNotFoundException {
        ApplicationUser user = applicationUserService.findByUsername(name);
        Test questionTest = testService.findById(questionDTO.getTestId());
        if(questionTest.getAuthor() != user) {
            throw new NotOwnerException();
        }
        Question oldQuestion = questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
        Question questionToSave = createFromDto(questionDTO);
        questionToSave.setId(oldQuestion.getId());
        questionRepository.save(questionToSave);
    }

    public void removeQuestion(long id, String name) throws QuestionNotFoundException, UserAccountNotFoundException, ApplicationUserNotFoundException, NotOwnerException {
        ApplicationUser user = applicationUserService.findByUsername(name);
        Question oldQuestion = findById(id);
        if(oldQuestion.getTest().getAuthor() != user) {
            throw new NotOwnerException();
        }
        questionRepository.delete(oldQuestion);
    }


    private Question createFromDto(QuestionCreateDTO questionDto) throws AnswerListEmptyException, EmptyQuestionContentException, QuestionTypeNotFoundException, TestNotFoundException {
        Question newQuestion = new Question();
        newQuestion.setTest(testService.findById(questionDto.getTestId()));

        Type questionType = questionDto.getType();
        if(questionType == null) {
            throw new QuestionTypeNotFoundException();
        } else {
            newQuestion.setType(questionType);
        }
        String questionContent = questionDto.getContent();
        if(questionContent == null || questionContent.isEmpty()) {
            throw new EmptyQuestionContentException();
        } else {
            newQuestion.setContent(questionContent);
        }
        if(questionType == Type.W) {
            Set<String> possibleAnswers = questionDto.getPossibleAnswers();
            if(possibleAnswers == null || possibleAnswers.size() < 2) {
                throw new AnswerListEmptyException();
            } else {
                newQuestion.setPossibleAnswers(possibleAnswers);
            }
        }
        return newQuestion;
    }

}
