package com.masiblue.backend.service;

import com.masiblue.backend.exception.*;
import com.masiblue.backend.model.ApplicationUser;
import com.masiblue.backend.model.RoleConstants;
import com.masiblue.backend.model.UserAccount;
import com.masiblue.backend.repository.ApplicationUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;
    private final UserAccountService userAccountService;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository, UserAccountService userAccountService) {
        this.applicationUserRepository = applicationUserRepository;
        this.userAccountService = userAccountService;
    }

    public void addNewUser(ApplicationUser user) throws ApplicationUserAlreadyExistsException {
        if(applicationUserRepository.findById(user.getId()).isPresent()) {
            throw new ApplicationUserAlreadyExistsException();
        } else {
            applicationUserRepository.save(user);
        }
    }

    public List<ApplicationUser> findAll() {
        return applicationUserRepository.findAll();
    }

    public List<ApplicationUser> findAllRedactors() {
        return applicationUserRepository.findAllByRoleNameEquals(RoleConstants.REDACTOR_ROLE);
    }

    public ApplicationUser findById(long id) throws ApplicationUserNotFoundException {
        return applicationUserRepository.findById(id).orElseThrow(ApplicationUserNotFoundException::new);
    }

    public ApplicationUser findRedactorById(long id) throws ApplicationUserNotFoundException, RedactorNotFoundException {
        ApplicationUser redactor = findById(id);
        if(!redactor.getRole().getName().equals(RoleConstants.REDACTOR_ROLE)) {
            throw new RedactorNotFoundException();
        }
        return redactor;
    }

    public ApplicationUser findByUsername(String username) throws UserAccountNotFoundException, ApplicationUserNotFoundException {
        UserAccount userAcc = userAccountService.findByUsername(username);
        return findById(userAcc.getId());
    }

    public ApplicationUser findRedactorByUsername(String username) throws UserAccountNotFoundException, ApplicationUserNotFoundException, RedactorNotFoundException {
        ApplicationUser redactor = findByUsername(username);
        if(!redactor.getRole().getName().equals(RoleConstants.REDACTOR_ROLE)) {
            throw new RedactorNotFoundException();
        }
        return redactor;
    }


    public boolean updateRedactor(long id, ApplicationUser newData) throws ApplicationUserNotFoundException, RedactorNotFoundException {
        newData.setId(id);
        ApplicationUser oldUser = findById(id);
        if(!oldUser.getRole().getName().equals(RoleConstants.REDACTOR_ROLE)) {
            throw new RedactorNotFoundException();
        }
        updateWithNewData(oldUser, newData);
        ApplicationUser updatedUser = applicationUserRepository.save(newData);
        return updatedUser != null;
    }

    public boolean update(long id, ApplicationUser newData) throws ApplicationUserNotFoundException {
        newData.setId(id);
        ApplicationUser oldUser = findById(id);
        updateWithNewData(oldUser, newData);
        ApplicationUser updatedUser = applicationUserRepository.save(newData);
        return updatedUser != null;
    }

    public boolean deleteRedactor(long id) throws ApplicationUserNotFoundException, RedactorNotFoundException, UserAccountNotFoundException {
        ApplicationUser redactorToDelete = applicationUserRepository.findById(id).orElseThrow(ApplicationUserNotFoundException::new);
        if(!redactorToDelete.getRole().getName().equals(RoleConstants.REDACTOR_ROLE)) {
            throw new RedactorNotFoundException();
        }
        userAccountService.deleteByApplicationUserId(id);
        return !applicationUserRepository.findById(id).isPresent();
    }

    public boolean delete(long id) throws ApplicationUserNotFoundException {
        if(!applicationUserRepository.findById(id).isPresent()) {
            throw new ApplicationUserNotFoundException();
        }
        applicationUserRepository.deleteById(id);
        return !applicationUserRepository.findById(id).isPresent();
    }

    private void updateWithNewData(ApplicationUser oldData, ApplicationUser newData) {
        if(newData.getLastName() == null)
            newData.setLastName(oldData.getLastName());
        if(newData.getFirstName() == null)
            newData.setFirstName(oldData.getFirstName());
        if(newData.getRole() == null)
            newData.setRole(oldData.getRole());
    }

    public boolean exists(ApplicationUser author) throws ApplicationUserNotFoundException {
        return applicationUserRepository.findById(author.getId()).orElseThrow(ApplicationUserNotFoundException::new).equals(author);
    }
}
