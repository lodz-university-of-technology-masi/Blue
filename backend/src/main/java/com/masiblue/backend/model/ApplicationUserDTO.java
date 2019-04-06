package com.masiblue.backend.model;

import java.util.ArrayList;
import java.util.List;

public class ApplicationUserDTO {

    public ApplicationUserDTO(ApplicationUser applicationUser) {
        this.username = applicationUser.getUsername();
        this.password = applicationUser.getPassword();
        roles = new ArrayList<>();
        for(Role role : applicationUser.getRoles()) {
            roles.add(role.getName());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String username;

    private List<String> roles;

    private String password;

}
