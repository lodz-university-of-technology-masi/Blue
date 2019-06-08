package com.masiblue.backend.controller;

import com.masiblue.backend.exception.*;
import com.masiblue.backend.model.Question;
import com.masiblue.backend.model.QuestionCreateDTO;
import com.masiblue.backend.repository.QuestionRepository;
import com.masiblue.backend.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PreAuthorize("hasAnyRole('USER','REDACTOR')")
    @GetMapping("/{id}")
    public ResponseEntity getSingleQuestion(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(questionService.findById(id), HttpStatus.OK);
        } catch (QuestionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question with this is does not exist");
        }
    }

    @PreAuthorize("hasRole('REDACTOR')")
    @PostMapping
    public ResponseEntity addNewQuestion(Authentication auth, @RequestBody QuestionCreateDTO question) {
        try {
            questionService.addNewQuestion(question, auth.getName());
            return new ResponseEntity<>("Ok", HttpStatus.OK);
        } catch (UserAccountNotFoundException | ApplicationUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        } catch (NotOwnerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only owner can add question to tests");
        } catch (QuestionTypeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This type of question is not supported");
        } catch (AnswerListEmptyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This type of question requires list of possible answers");
        } catch (EmptyQuestionContentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question has to have content");
        } catch (TestNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test does not exist");
        }
    }

    @PreAuthorize("hasRole('REDACTOR')")
    @PutMapping("/{id}")
    public ResponseEntity updateQuestion(Authentication auth, @RequestBody QuestionCreateDTO question, @PathVariable("id") long id) {
        try {
            questionService.updateQuestion(question, id, auth.getName());
            return new ResponseEntity<>("Successfully updated", HttpStatus.OK);
        } catch (UserAccountNotFoundException | ApplicationUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        } catch (NotOwnerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only owner can modify question");
        } catch (QuestionTypeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This type of question is not supported");
        } catch (AnswerListEmptyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This type of question requires list of possible answers");
        } catch (EmptyQuestionContentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question has to have content");
        } catch (QuestionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question with this id does not exist");
        } catch (TestNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test does not exist");
        }
    }

    @PreAuthorize("hasRole('REDACTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity removeQuestion(Authentication auth, @PathVariable("id") long id) {
        try {
            questionService.removeQuestion(id, auth.getName());
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        } catch (QuestionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question with this id does not exist");
        } catch (UserAccountNotFoundException | ApplicationUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        } catch (NotOwnerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only owner can modify question");
        }
    }
}
