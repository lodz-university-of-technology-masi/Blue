package com.masiblue.backend.model;

import lombok.Data;

@Data
public class TestLanguagePositionDTO {

    public TestLanguagePositionDTO(Test testData) {
        this.position = testData.getPosition().getId();
        this.language = testData.getLanguage().getId();
    }

    private long id;
    private long position;
    private long language;
}
