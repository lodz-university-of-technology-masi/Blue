package com.masiblue.backend.controller;

import com.masiblue.backend.exception.ApplicationUserNotFoundException;
import com.masiblue.backend.exception.RedactorNotFoundException;
import com.masiblue.backend.exception.UserAccountAlreadyExistsException;
import com.masiblue.backend.exception.UserAccountNotFoundException;
import com.masiblue.backend.model.ApplicationUser;
import com.masiblue.backend.model.UserAccountDTO;
import com.masiblue.backend.service.ApplicationUserService;
import com.masiblue.backend.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;
    private final UserAccountService userAccountService;

    public ApplicationUserController(ApplicationUserService applicationUserService, UserAccountService userAccountService) {
        this.applicationUserService = applicationUserService;
        this.userAccountService = userAccountService;
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping
    public List listUsers() {
        return applicationUserService.findAll();
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/redactors")
    public ResponseEntity<String> addRedactor(@RequestBody UserAccountDTO userAccountDTO) {
        try {
            userAccountService.addNewRedactor(userAccountDTO);
            return new ResponseEntity<>("Successfully created new redactor", HttpStatus.OK);
        } catch (UserAccountAlreadyExistsException ex) {
            return new ResponseEntity<>("Account with this username already exists", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @GetMapping("/redactors")
    public List listRedactors() {
        return applicationUserService.findAllRedactors();
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/redactors/{id}")
    public ResponseEntity listSingleRedactor(@PathVariable("id") long id) {
        try {
            ApplicationUser redactor = applicationUserService.findRedactorById(id);
            return new ResponseEntity<>(redactor, HttpStatus.OK);
        } catch (ApplicationUserNotFoundException ex) {
            return new ResponseEntity<>("There is no user with this id", HttpStatus.BAD_REQUEST);
        } catch (RedactorNotFoundException e) {
            return new ResponseEntity<>("User with this id is not a redactor", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping("/redactors/{id}")
    public ResponseEntity<String> updateRedactor(@PathVariable("id") long id, @RequestBody ApplicationUser newUserData) {
        try {
            applicationUserService.updateRedactor(id, newUserData);
            return new ResponseEntity<>("Successfully updated redactor data", HttpStatus.OK);
        } catch (RedactorNotFoundException e) {
            return new ResponseEntity<>("User with this id is not a redactor", HttpStatus.BAD_REQUEST);
        } catch (ApplicationUserNotFoundException e) {
            return new ResponseEntity<>("There is no user with this id", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/redactors/{id}")
    public ResponseEntity<String> deleteRedactor(@PathVariable("id") long id) {
        try {
            applicationUserService.deleteRedactor(id);
            return new ResponseEntity<>("Successfully deleted redactor data", HttpStatus.OK);
        } catch (RedactorNotFoundException e) {
            return new ResponseEntity<>("User with this id is not a redactor", HttpStatus.BAD_REQUEST);
        } catch (ApplicationUserNotFoundException e) {
            return new ResponseEntity<>("There is no user with this id", HttpStatus.BAD_REQUEST);
        } catch (UserAccountNotFoundException e) {
            return new ResponseEntity<>("User with this id has no account", HttpStatus.BAD_REQUEST);
        }
    }

}
