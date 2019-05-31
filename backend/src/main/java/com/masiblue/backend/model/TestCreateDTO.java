package com.masiblue.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class TestCreateDTO {
    private String name;
    private long positionId;
    private long languageId;

    public TestCreateDTO(String name, long positionId) {
        this.name = name;
        this.positionId = positionId;
    }
}
