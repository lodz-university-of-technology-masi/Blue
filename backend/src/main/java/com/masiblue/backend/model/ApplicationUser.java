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
//@Table(name = "account")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable (name = "application_user_roles",
//                joinColumns =  @JoinColumn(name = "application_user_id", referencedColumnName = "id"),
//                inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
//    private Set<Role> roles;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Role role;

//    private boolean confirmed;
//    private boolean active;



}
