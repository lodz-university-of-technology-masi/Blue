package com.masiblue.backend.service;

import com.masiblue.backend.model.ApplicationUser;
import com.masiblue.backend.model.RoleConstants;
import com.masiblue.backend.repository.ApplicationUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    public void save(ApplicationUser user) {
        applicationUserRepository.save(user);
    }

    public List<ApplicationUser> findAll() {
        return applicationUserRepository.findAll();
    }

    public List<ApplicationUser> findAllModerators() {
        return applicationUserRepository.findAllByRoleNameEquals(RoleConstants.MODERATOR_ROLE);
    }

    public ApplicationUser findById(long id) {
        return applicationUserRepository.findById(id).orElse(null);
    }

    public boolean update(long id, ApplicationUser newData) {
        newData.setId(id);
        ApplicationUser oldUser = applicationUserRepository.findById(id).orElse(null);
        if(oldUser == null) {
            return false;
        } else {
            if(newData.getLastName() == null)
                newData.setLastName(oldUser.getLastName());
            if(newData.getFirstName() == null)
                newData.setFirstName(oldUser.getFirstName());
            if(newData.getRole() == null)
                newData.setRole(oldUser.getRole());
        }
        ApplicationUser updatedUser = applicationUserRepository.save(newData);
        return updatedUser != null;
    }

    public boolean delete(long id) {
        applicationUserRepository.deleteById(id);
        return !applicationUserRepository.findById(id).isPresent();
    }

}
