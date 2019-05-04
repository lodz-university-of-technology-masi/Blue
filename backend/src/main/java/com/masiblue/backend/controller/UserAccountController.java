package com.masiblue.backend.controller;

import com.masiblue.backend.exception.UserAccountAlreadyExistsException;
import com.masiblue.backend.model.ApplicationUser;
import com.masiblue.backend.model.Role;
import com.masiblue.backend.model.UserAccount;
import com.masiblue.backend.model.UserAccountDTO;
import com.masiblue.backend.service.RoleService;
import com.masiblue.backend.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService, RoleService roleService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody UserAccountDTO userAccountDTO) {
        try {
            userAccountService.addNewAccount(userAccountDTO);
            return new ResponseEntity<>("Successfully created new user", HttpStatus.OK);
        } catch (UserAccountAlreadyExistsException e) {
            return new ResponseEntity<>("Username is already in use", HttpStatus.BAD_REQUEST);
        }
    }
}
