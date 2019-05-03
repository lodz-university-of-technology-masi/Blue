package com.masiblue.backend.repository;

import com.masiblue.backend.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    List<ApplicationUser> findAllByRoleNameEquals(String roleName);
}
