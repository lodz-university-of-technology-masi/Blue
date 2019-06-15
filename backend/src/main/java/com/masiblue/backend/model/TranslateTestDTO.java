package com.masiblue.backend.model;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class TranslateTestDTO {
    private long id;
    private long languageId;
    private String testName;
}
