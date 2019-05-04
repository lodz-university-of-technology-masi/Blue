package com.masiblue.backend.service;

import com.masiblue.backend.exception.UserAccountAlreadyExistsException;
import com.masiblue.backend.exception.UserAccountNotFoundException;
import com.masiblue.backend.model.*;
import com.masiblue.backend.repository.UserAccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void addNewModerator(UserAccountDTO accountData) throws UserAccountAlreadyExistsException {
        Role moderatorRole = roleService.findByName(RoleConstants.MODERATOR_ROLE);
        if(moderatorRole == null) {
            moderatorRole = new Role(RoleConstants.MODERATOR_ROLE);
        }

        ApplicationUser newModerator = new ApplicationUser(accountData.getFirstName(), accountData.getLastName(), moderatorRole);
        UserAccount newModeratorAccount = new UserAccount(accountData.getUsername(), accountData.getPassword(), newModerator);
        addNewAccount(newModeratorAccount);
    }

    public void addNewAccount(UserAccountDTO accountData) throws UserAccountAlreadyExistsException {
        Role noneRole = roleService.findByName("NONE");
        if(noneRole == null) {
            noneRole = new Role();
            noneRole.setName("NONE");
        }

        ApplicationUser newUser = new ApplicationUser(accountData.getFirstName(), accountData.getLastName(), noneRole);
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
