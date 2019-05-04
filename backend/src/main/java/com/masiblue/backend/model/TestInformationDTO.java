package com.masiblue.backend.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestInformationDTO {

    public TestInformationDTO(Test testData) {
        this.id = testData.getId();
        this.testName = testData.getName();
        this.authorFirstName = testData.getAuthor().getFirstName();
        this.authorLastName = testData.getAuthor().getLastName();
        this.position = testData.getPosition().getName();
        this.creationDate = testData.getCreationDate();
        this.modifiedDate = testData.getModificationDate();
        this.language = testData.getLanguage().getName();
    }

    private long id;
    private String testName;
    private String authorFirstName;
    private String authorLastName;
    private String position;
    private LocalDateTime creationDate;
    private LocalDateTime modifiedDate;
    private String language;
}
