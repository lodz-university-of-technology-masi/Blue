package com.masiblue.backend.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsvQuestionModel {

    @CsvBindByPosition(position = 0)
    private int number;

    @CsvBindByPosition(position = 1)
    private String type;

    @CsvBindByPosition(position = 2)
    private String language;

    @CsvBindByPosition(position = 3)
    private String content;

    @CsvBindByPosition(position = 4)
    private int numberOfPossibleAnswers;

    @CsvBindByPosition(position = 5)
    private String possibleAnswers;

}