package com.masiblue.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = "users")
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    public Role(String name) {
        this.name = name;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "role",
    cascade = CascadeType.PERSIST)
    private Set<ApplicationUser> users = new HashSet<>();

}
