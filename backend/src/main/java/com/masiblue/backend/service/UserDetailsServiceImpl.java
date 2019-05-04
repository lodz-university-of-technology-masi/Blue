package com.masiblue.backend.service;

import com.masiblue.backend.exception.UserAccountNotFoundException;
import com.masiblue.backend.model.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserAccountService userAccountService;

    public UserDetailsServiceImpl(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserAccount user = userAccountService.findByUsername(username);
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            grantedAuthorities.add(new SimpleGrantedAuthority((user.getApplicationUser().getRole().getName())));

            return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
        } catch (UserAccountNotFoundException e) {
            throw new UsernameNotFoundException(username);
        }
    }
}
