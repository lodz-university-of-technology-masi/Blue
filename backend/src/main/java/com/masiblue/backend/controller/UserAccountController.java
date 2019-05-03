package com.masiblue.backend.controller;

import com.masiblue.backend.model.ApplicationUser;
import com.masiblue.backend.model.Role;
import com.masiblue.backend.model.UserAccount;
import com.masiblue.backend.model.UserAccountDTO;
import com.masiblue.backend.service.RoleService;
import com.masiblue.backend.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final RoleService roleService;

    public UserAccountController(UserAccountService userAccountService, RoleService roleService) {
        this.userAccountService = userAccountService;
        this.roleService = roleService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody UserAccountDTO userAccountDTO) {
        if(userAccountService.findByUsername(userAccountDTO.getUsername()) != null) {
            return new ResponseEntity<>("Username is already in use", HttpStatus.BAD_REQUEST);
        }

        Role noneRole = roleService.findByName("NONE");
        if(noneRole == null) {
            noneRole = new Role();
            noneRole.setName("NONE");
        }

        ApplicationUser newUser = new ApplicationUser(userAccountDTO.getFirstName(), userAccountDTO.getLastName(), noneRole);
        UserAccount newUserAccount = new UserAccount(userAccountDTO.getUsername(), userAccountDTO.getPassword(), newUser);
        userAccountService.save(newUserAccount);
        return new ResponseEntity<>("Successfully created new user", HttpStatus.OK);
    }
}
