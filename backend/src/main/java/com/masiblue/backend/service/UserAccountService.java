package com.masiblue.backend.service;

import com.masiblue.backend.model.UserAccount;
import com.masiblue.backend.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccountService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserAccountRepository userAccountRepository;

    public UserAccountService(BCryptPasswordEncoder bCryptPasswordEncoder, UserAccountRepository userAccountRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userAccountRepository = userAccountRepository;
    }

    public void save(UserAccount user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userAccountRepository.save(user);
    }

    public boolean delete(long id) {
        userAccountRepository.deleteById(id);
        return !userAccountRepository.findById(id).isPresent();
    }

    public Optional<UserAccount> findById(long id) {
        return userAccountRepository.findById(id);
    }

    public UserAccount findByUsername(String username) {
        return userAccountRepository.findByUsername(username);
    }

    public Optional<UserAccount> findByApplicationUserId(long id) {
        return userAccountRepository.findByApplicationUser_Id(id);
    }
}
