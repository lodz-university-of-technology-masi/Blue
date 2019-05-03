package com.masiblue.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(exclude = "applicationUser")
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {

    public UserAccount(String username, String password, ApplicationUser applicationUser) {
        this.username = username;
        this.password = password;
        this.applicationUser = applicationUser;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String password;

    @OneToOne
    @Cascade(CascadeType.ALL)
    ApplicationUser applicationUser;
}
