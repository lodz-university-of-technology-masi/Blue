package com.masiblue.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class QuestionCreateDTO {
    private Type type;
    private long languageId;
    private long testId;
    private String content;
    private Set<String> possibleAnswers;
}
