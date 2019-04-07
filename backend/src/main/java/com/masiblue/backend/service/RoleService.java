package com.masiblue.backend.service;

import com.masiblue.backend.model.Role;
import com.masiblue.backend.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(long Id) {
        return roleRepository.findById(Id).orElse(null);
    }
}
