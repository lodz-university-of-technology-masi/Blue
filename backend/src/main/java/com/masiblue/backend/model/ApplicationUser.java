package com.masiblue.backend.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Set;


@Entity
@Data
@EqualsAndHashCode(exclude = "roles")
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "account")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable (name = "application_user_roles",
                joinColumns =  @JoinColumn(name = "application_user_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

//    private boolean confirmed;
//    private boolean active;



}
