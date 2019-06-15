package com.masiblue.backend.controller;

import com.masiblue.backend.exception.*;
import com.masiblue.backend.model.Test;
import com.masiblue.backend.model.TestCreateDTO;
import com.masiblue.backend.model.TranslateTestDTO;
import com.masiblue.backend.service.TestService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public Test listSingleTest(@PathVariable("id") long id) {
        try {
            return testService.findById(id);
        } catch (TestNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no test with this id", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_REDACTOR','ROLE_MODERATOR')")
    @PutMapping("/{id}")
    public ResponseEntity updateSingleTest(Authentication auth, @PathVariable("id") long id, @RequestBody TestCreateDTO testInformation) {
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

    @PreAuthorize("hasAnyRole('ROLE_REDACTOR','ROLE_MODERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteSingleTest(Authentication auth, @PathVariable("id") long id) {
        try {
            testService.deleteTest(id, auth.getName());
            return new ResponseEntity<>("Successfully deleted test", HttpStatus.OK);
        } catch (TestNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no test with this id", e);
        } catch (UserAccountNotFoundException | ApplicationUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this token does not exist", e);
        } catch (NotOwnerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This redactor is not owner of this test", e);
        }
    }


        @PreAuthorize("hasAnyRole('ROLE_REDACTOR','ROLE_MODERATOR')")
    @GetMapping("/solvelist/{idlang}/{idpos}")
    public List listTestsForSolve(@PathVariable("idlang") long languageId, @PathVariable("idpos") long positionId){
        return testService.findAllByLangAndPos(languageId, positionId);
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

    @PreAuthorize("hasAnyRole('ROLE_REDACTOR')")
    @PostMapping(value = "/csv/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity importTest(Authentication auth, @RequestParam("testName") String testName,
                                     @RequestParam("positionId") long positionId, @RequestParam("file") MultipartFile file) {
        try {
            TestCreateDTO testCreateDTO = new TestCreateDTO(testName, positionId);
            testService.importTestFromCsv(testCreateDTO, auth.getName(), file);
            return new ResponseEntity<>("Test successfully imported.", HttpStatus.OK);
        } catch (UserAccountNotFoundException | ApplicationUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this token does not exist", e);
        } catch (InvalidCsvException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CSV file format", e);
        } catch (PositionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Position with this id does not exist", e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_REDACTOR')")
    @GetMapping(value = "csv/export/{testId}")
    public void exportTest(@PathVariable long testId, HttpServletResponse response) throws IOException {
        try {
            testService.exportTestToCsv(testId, response);
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "There was a backend error with parsing the test to the CSV file", e);
        } catch (TestNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test with this id does not exist", e);
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_REDACTOR','ROLE_MODERATOR','ROLE_USER')")
    @GetMapping("/questions/{id}")
    public List getQuestionForTest(Authentication auth, @PathVariable("id") long testId) {
        try {
            return testService.getQuestionsForId(testId, auth.getName());
        } catch (TestNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no test with this id", e);
        } catch (UserAccountNotFoundException | ApplicationUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this token does not exist", e);
        } catch (NotOwnerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This redactor is not owner of this test", e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_REDACTOR','ROLE_MODERATOR','ROLE_USER')")
    @PostMapping("/translate")
    public ResponseEntity translateTest(Authentication auth, @RequestBody TranslateTestDTO translateTestDTO) {
        try {
            testService.translateTest(translateTestDTO, auth.getName());
            return new ResponseEntity<>("Successfully created test", HttpStatus.OK);
        } catch (TestNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no test with this id", e);
        } catch (UserAccountNotFoundException | ApplicationUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this token does not exist", e);
        } catch (NotOwnerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This redactor is not owner of this test", e);
        } catch (LanguageNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no such language", e);
        } catch (LanguageTranslationNotSupportedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This language is not supported by translation feature", e);
        } catch (LanguageAlreadExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot translate test to the same language", e);
        }
    }

}
