package com.masiblue.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Test {

    public Test(String name, Position position, ApplicationUser author, Language language) {
        this.name = name;
        this.position = position;
        this.author = author;
        this.language = language;
        this.creationDate = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToOne
    @JoinColumn
    private Position position;

    @ManyToOne
    @JoinColumn
    private ApplicationUser author;

    @ManyToOne
    @JoinColumn
    private Language language;

    private LocalDateTime creationDate;

    private LocalDateTime modificationDate;

//
//    private List<Question> questions;
}
