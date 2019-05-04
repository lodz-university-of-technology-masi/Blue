package com.masiblue.backend.service;

import com.masiblue.backend.exception.ApplicationUserAlreadyExistsException;
import com.masiblue.backend.exception.ApplicationUserNotFoundException;
import com.masiblue.backend.exception.RedactorNotFoundException;
import com.masiblue.backend.model.ApplicationUser;
import com.masiblue.backend.model.RoleConstants;
import com.masiblue.backend.repository.ApplicationUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
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

    public boolean deleteRedactor(long id) throws ApplicationUserNotFoundException, RedactorNotFoundException {
        ApplicationUser redactorToDelete = applicationUserRepository.findById(id).orElseThrow(ApplicationUserNotFoundException::new);
        if(!redactorToDelete.getRole().getName().equals(RoleConstants.REDACTOR_ROLE)) {
            throw new RedactorNotFoundException();
        }
        applicationUserRepository.deleteById(id);
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
}
