package com.masiblue.backend.controller;

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
    private final RoleService roleService;

    public ModeratorController(ApplicationUserService applicationUserService, UserAccountService userAccountService, RoleService roleService) {
        this.applicationUserService = applicationUserService;
        this.userAccountService = userAccountService;
        this.roleService = roleService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<String> addModerator(@RequestBody UserAccountDTO userAccountDTO) {
        if(userAccountService.findByUsername(userAccountDTO.getUsername()) != null) {
            return new ResponseEntity<>("Username is already in use", HttpStatus.BAD_REQUEST);
        }

        Role moderatorRole = roleService.findByName(RoleConstants.MODERATOR_ROLE);
        if(moderatorRole == null) {
            moderatorRole = new Role(RoleConstants.MODERATOR_ROLE);
        }

        ApplicationUser newModerator = new ApplicationUser(userAccountDTO.getFirstName(), userAccountDTO.getLastName(), moderatorRole);
        UserAccount newModeratorAccount = new UserAccount(userAccountDTO.getUsername(), userAccountDTO.getPassword(), newModerator);
        userAccountService.save(newModeratorAccount);
        return new ResponseEntity<>("Successfully created new moderator", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public List listModerators() {
        return applicationUserService.findAllModerators();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity listSingleModerator(@PathVariable("id") long id) {
        ApplicationUser moderator = applicationUserService.findById(id);
        Optional<ResponseEntity<String>> validation = validateModerator(moderator);
        if(validation.isPresent()) {
            return validation.get();
        }
        return new ResponseEntity<ApplicationUser>(moderator, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateModerator(@PathVariable("id") long id, @RequestBody ApplicationUser newUserData) {
        ApplicationUser userToUpdate = applicationUserService.findById(id);
        Optional<ResponseEntity<String>> validation = validateModerator(userToUpdate);
        if(validation.isPresent()) {
            return validation.get();
        }
        if(applicationUserService.update(id, newUserData)) {
            return new ResponseEntity<>("Succesfully updated moderator data", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not update moderator data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteModerator(@PathVariable("id") long id) {
        ApplicationUser userToDelete = applicationUserService.findById(id);
        Optional<ResponseEntity<String>> validation = validateModerator(userToDelete);
        if(validation.isPresent()) {
            return validation.get();
        }
        Optional<UserAccount> userAccount = userAccountService.findByApplicationUserId(userToDelete.getId());
        if(!userAccount.isPresent()) {
            return new ResponseEntity<>("Could not delete moderator account", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(userAccountService.delete(userAccount.get().getId())) {
            return new ResponseEntity<>("Succesfully deleted moderator", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not delete moderator", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Optional<ResponseEntity<String>> validateModerator(ApplicationUser user) {
        if(user == null) {
            return Optional.of(new ResponseEntity<>("There is no user with this id", HttpStatus.BAD_REQUEST));
        }
        if(!user.getRole().getName().equals(RoleConstants.MODERATOR_ROLE)) {
            return Optional.of(new ResponseEntity<>("User with this id is not a moderator", HttpStatus.BAD_REQUEST));
        }
        return Optional.empty();
    }
}
