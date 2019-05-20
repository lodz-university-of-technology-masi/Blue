package com.masiblue.backend.controller;

import com.masiblue.backend.exception.*;
import com.masiblue.backend.model.Test;
import com.masiblue.backend.model.TestCreateDTO;
import com.masiblue.backend.model.TestInformationDTO;
import com.masiblue.backend.model.TestSolveDTO;
import com.masiblue.backend.service.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public List listAllTests() {
//        return testService.findAll().stream().map(TestInformationDTO::new).collect(Collectors.toList());
        return testService.findAll();
    }

    @PostMapping
    public ResponseEntity createNewTest(@RequestBody TestCreateDTO testInformation) {
        try {
            testService.addNewTest(testInformation);
            return new ResponseEntity<>("Successfully created new test", HttpStatus.OK);
        } catch (RedactorNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this id is not a redactor", ex);
        } catch (ApplicationUserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no user with this id", ex);
        } catch (LanguageNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no language with this id", ex);
        } catch (PositionNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no position with this id", ex);
        }
    }

    @GetMapping("/{id}")
    public TestInformationDTO listSingleTest(@PathVariable("id") long id) {
        try {
            Test test = testService.findById(id);
            return new TestInformationDTO(test);
        } catch (TestNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no test with this id", ex);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateSingleTest(@PathVariable("id") long id, @RequestBody Test testInformation) {
        try {
            testService.update(id, testInformation);
            return new ResponseEntity<>("Successfully updated test", HttpStatus.OK);
        } catch (TestNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no test with this id", e);
        } catch (LanguageNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no such language", e);
        } catch (PositionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no such position", e);
        }
    }

    @GetMapping("/solvelist/{idlang}/{idpos}")
    public List listTestsForSolve(@PathVariable("idlang") long languageId, @PathVariable("idpos") long positionId){
        return testService.findAllByLangAndPos(languageId, positionId);
    }

    @GetMapping("/position/{id}")
    public List listTestsForPosition(@PathVariable("id") long positionId) {
        //TODO: consider if we should return error if there is no position with this id
        return testService.findAllForPositionId(positionId);
    }

    @GetMapping("/language/{id}")
    public List listTestsForLanguage(@PathVariable("id") long languageId) {
        //TODO: consider if we should return error if there is no language with this id
        return testService.findAllForLanguageId(languageId);
    }

    @GetMapping("/moderator/{id}")
    public List listTestsForModerator(@PathVariable("id") long moderatorId) {
        //TODO: consider if we should return error if there is no moderator with this id
        return testService.findAllForModeratorId(moderatorId);
    }
}
