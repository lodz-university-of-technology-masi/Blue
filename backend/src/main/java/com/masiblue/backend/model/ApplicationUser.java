package com.masiblue.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(exclude = "role")
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser {

    public ApplicationUser(String firstName, String lastName, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;

    private String lastName;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn
    private Role role;
}
