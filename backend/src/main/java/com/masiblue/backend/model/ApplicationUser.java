package com.masiblue.backend.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(exclude = "role")
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    @ManyToOne
    @JoinColumn
    private Role role;
}
