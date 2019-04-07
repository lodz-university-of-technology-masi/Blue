package com.masiblue.backend.model;

public class RoleDTO {

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private long id;

    private String name;
}
