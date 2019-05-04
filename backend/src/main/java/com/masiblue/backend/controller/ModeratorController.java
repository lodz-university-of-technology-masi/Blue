package com.masiblue.backend.controller;

import com.masiblue.backend.exception.ApplicationUserNotFoundException;
import com.masiblue.backend.exception.ModeratorNotFoundException;
import com.masiblue.backend.exception.UserAccountAlreadyExistsException;
import com.masiblue.backend.model.*;
import com.masiblue.backend.service.ApplicationUserService;
import com.masiblue.backend.service.RoleService;
import com.masiblue.backend.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/moderators")
public class ModeratorController {
    private final ApplicationUserService applicationUserService;
    private final UserAccountService userAccountService;

    public ModeratorController(ApplicationUserService applicationUserService, UserAccountService userAccountService, RoleService roleService) {
        this.applicationUserService = applicationUserService;
        this.userAccountService = userAccountService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<String> addModerator(@RequestBody UserAccountDTO userAccountDTO) {
        try {
            userAccountService.addNewModerator(userAccountDTO);
            return new ResponseEntity<>("Successfully created new moderator", HttpStatus.OK);
        } catch (UserAccountAlreadyExistsException ex) {
            return new ResponseEntity<>("Account with this username already exists", HttpStatus.BAD_REQUEST);
        }


    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public List listModerators() {
        return applicationUserService.findAllModerators();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity listSingleModerator(@PathVariable("id") long id) {
        try {
            ApplicationUser moderator = applicationUserService.findModeratorById(id);
            return new ResponseEntity<>(moderator, HttpStatus.OK);
        } catch (ApplicationUserNotFoundException ex) {
            return new ResponseEntity<>("There is no user with this id", HttpStatus.BAD_REQUEST);
        } catch (ModeratorNotFoundException e) {
            return new ResponseEntity<>("User with this id is not a moderator", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateModerator(@PathVariable("id") long id, @RequestBody ApplicationUser newUserData) {
        try {
            applicationUserService.updateModerator(id, newUserData);
            return new ResponseEntity<>("Successfully updated moderator data", HttpStatus.OK);
        } catch (ModeratorNotFoundException e) {
            return new ResponseEntity<>("User with this id is not a moderator", HttpStatus.BAD_REQUEST);
        } catch (ApplicationUserNotFoundException e) {
            return new ResponseEntity<>("There is no user with this id", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteModerator(@PathVariable("id") long id) {
        try {
            applicationUserService.deleteModerator(id);
            return new ResponseEntity<>("Successfully deleted moderator data", HttpStatus.OK);
        } catch (ModeratorNotFoundException e) {
            return new ResponseEntity<>("User with this id is not a moderator", HttpStatus.BAD_REQUEST);
        } catch (ApplicationUserNotFoundException e) {
            return new ResponseEntity<>("There is no user with this id", HttpStatus.BAD_REQUEST);
        }
    }

}
