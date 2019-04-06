package com.masiblue.backend.service;

import com.masiblue.backend.model.ApplicationUser;
import com.masiblue.backend.repository.ApplicationUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationUserService {

    private final ApplicationUserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApplicationUserService(ApplicationUserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void save(ApplicationUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public ApplicationUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<ApplicationUser> findAll() {
        return userRepository.findAll();
    }

}
