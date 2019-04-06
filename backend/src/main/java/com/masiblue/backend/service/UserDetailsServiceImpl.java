package com.masiblue.backend.service;

import com.masiblue.backend.model.ApplicationUser;
import com.masiblue.backend.model.Role;
import com.masiblue.backend.repository.ApplicationUserRepository;
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

    private ApplicationUserService applicationUserService;

    public UserDetailsServiceImpl(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = applicationUserService.findByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for(Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority((role.getName())));
        }

        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
