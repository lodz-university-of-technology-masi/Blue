package com.masiblue.backend.service;

import com.masiblue.backend.model.ApplicationUser;
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

}
