package com.masiblue.backend.controller;

import com.masiblue.backend.exception.*;
import com.masiblue.backend.model.Test;
import com.masiblue.backend.model.TestCreateDTO;
import com.masiblue.backend.model.TestInformationDTO;
import com.masiblue.backend.service.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_REDACTOR')")
    public List listAllTests(Authentication auth)  {
        try {
            return testService.findAllByUsername(auth.getName());
        } catch (UserAccountNotFoundException | ApplicationUserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this token does not exist", ex);
        } catch (AuthorizationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization exception", ex);
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_REDACTOR')")
    public ResponseEntity createNewTest(Authentication auth, @RequestBody TestCreateDTO testInformation) {
        try {
            testService.addNewTest(testInformation, auth.getName());
            return new ResponseEntity<>("Successfully created new test", HttpStatus.OK);
        } catch (RedactorNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this token is not a redactor", ex);
        } catch (ApplicationUserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no user with this id", ex);
        } catch (LanguageNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no language with this id", ex);
        } catch (PositionNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no position with this id", ex);
        } catch (UserAccountNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this token does not exist", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_REDACTOR','ROLE_MODERATOR')")
    @GetMapping("/{id}")
    public TestInformationDTO listSingleTest(@PathVariable("id") long id) {
        try {
            Test test = testService.findById(id);
            return new TestInformationDTO(test);
        } catch (TestNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no test with this id", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_REDACTOR','ROLE_MODERATOR')")
    @PutMapping("/{id}")
    public ResponseEntity updateSingleTest(Authentication auth, @PathVariable("id") long id, @RequestBody Test testInformation) {
        try {
            testService.update(id, testInformation, auth.getName());
            return new ResponseEntity<>("Successfully updated test", HttpStatus.OK);
        } catch (TestNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no test with this id", e);
        } catch (LanguageNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no such language", e);
        } catch (PositionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no such position", e);
        } catch (ApplicationUserNotFoundException | UserAccountNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this token does not exist", ex);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization exception", e);
        } catch (NotOwnerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This redactor is not owner of this test", e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_REDACTOR','ROLE_MODERATOR','ROLE_USER')")
    @GetMapping("/position/{id}")
    public List listTestsForPosition(Authentication auth, @PathVariable("id") long positionId) {
        try {
            return testService.findAllForPositionId(positionId, auth.getName());
        } catch (UserAccountNotFoundException | ApplicationUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this token does not exist", e);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization exception", e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_REDACTOR','ROLE_MODERATOR','ROLE_USER')")
    @GetMapping("/language/{id}")
    public List listTestsForLanguage(Authentication auth, @PathVariable("id") long languageId) {
        try {
            return testService.findAllForLanguageId(languageId, auth.getName());
        } catch (UserAccountNotFoundException | ApplicationUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this token does not exist", e);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization exception", e);
        }
    }

}
