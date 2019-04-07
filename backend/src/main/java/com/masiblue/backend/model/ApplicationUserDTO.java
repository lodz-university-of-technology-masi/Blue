package com.masiblue.backend.model;

import java.util.ArrayList;
import java.util.List;

public class ApplicationUserDTO {

    public ApplicationUserDTO(ApplicationUser applicationUser) {
//        this.username = applicationUser.getUsername();
        this.ID = applicationUser.getId();
        this.firstName = applicationUser.getFirstName();
        this.lastName = applicationUser.getLastName();
        this.role = applicationUser.getRole().getName();
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public List<String> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(List<String> roles) {
//        this.roles = roles;
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String surname) {
        this.lastName = surname;
    }

    private long ID;

    private String username;

    private String firstName;

    private String lastName;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String role;

}
