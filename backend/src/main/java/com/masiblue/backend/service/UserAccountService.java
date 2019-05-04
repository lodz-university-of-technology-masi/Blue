package com.masiblue.backend.service;

import com.masiblue.backend.exception.UserAccountAlreadyExistsException;
import com.masiblue.backend.exception.UserAccountNotFoundException;
import com.masiblue.backend.model.*;
import com.masiblue.backend.repository.UserAccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserAccountRepository userAccountRepository;
    private final RoleService roleService;

    public UserAccountService(BCryptPasswordEncoder bCryptPasswordEncoder, UserAccountRepository userAccountRepository, RoleService roleService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userAccountRepository = userAccountRepository;
        this.roleService = roleService;
    }

    public void addNewRedactor(UserAccountDTO accountData) throws UserAccountAlreadyExistsException {
        Role redactorRole = roleService.findByName(RoleConstants.REDACTOR_ROLE);
        if(redactorRole == null) {
            redactorRole = new Role(RoleConstants.REDACTOR_ROLE);
        }

        ApplicationUser newModerator = new ApplicationUser(accountData.getFirstName(), accountData.getLastName(), redactorRole);
        UserAccount newModeratorAccount = new UserAccount(accountData.getUsername(), accountData.getPassword(), newModerator);
        addNewAccount(newModeratorAccount);
    }

    public void addNewAccount(UserAccountDTO accountData) throws UserAccountAlreadyExistsException {
        Role userRole = roleService.findByName(RoleConstants.USER_ROLE);
        if(userRole == null) {
            userRole = new Role();
            userRole.setName(RoleConstants.USER_ROLE);
        }

        ApplicationUser newUser = new ApplicationUser(accountData.getFirstName(), accountData.getLastName(), userRole);
        UserAccount newUserAccount = new UserAccount(accountData.getUsername(), accountData.getPassword(), newUser);
        addNewAccount(newUserAccount);
    }

    public void addNewAccount(UserAccount user) throws UserAccountAlreadyExistsException {
        if(userAccountRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAccountAlreadyExistsException();
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userAccountRepository.save(user);
    }

    public boolean delete(long id) {
        userAccountRepository.deleteById(id);
        return !userAccountRepository.findById(id).isPresent();
    }

    public void deleteByApplicationUserId(long id) throws UserAccountNotFoundException {
        UserAccount userToDelete = userAccountRepository.findByApplicationUser_Id(id).orElseThrow(UserAccountNotFoundException::new);
        userAccountRepository.delete(userToDelete);
    }

    public UserAccount findById(long id) throws UserAccountNotFoundException {
        return userAccountRepository.findById(id).orElseThrow(UserAccountNotFoundException::new);
    }

    public UserAccount findByUsername(String username) throws UserAccountNotFoundException {
        return userAccountRepository.findByUsername(username).orElseThrow(UserAccountNotFoundException::new);
    }

    public UserAccount findByApplicationUserId(long id) throws UserAccountNotFoundException {
        return userAccountRepository.findByApplicationUser_Id(id).orElseThrow(UserAccountNotFoundException::new);
    }
}
