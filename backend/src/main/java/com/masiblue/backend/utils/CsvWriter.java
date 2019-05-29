package com.masiblue.backend.utils;

import com.masiblue.backend.model.Question;
import com.masiblue.backend.model.Test;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class CsvWriter {

    private static final String ENGLISH_LANG = "EN";
    private static final String POLISH_LANG = "PL";

    public static List<CsvQuestionModel> exportToCsv(Test test) {
        List<Question> questions = test.getQuestions();
        List<CsvQuestionModel> csvQuestions = new ArrayList<>();
        int numberIterator = 0;
        for (Question q : questions) {
            CsvQuestionModel csvQuestion = new CsvQuestionModel();
            csvQuestion.setNumber(numberIterator);
            csvQuestion.setContent(q.getContent());
            csvQuestion.setLanguage(parseLanguageToCsv(test.getLanguage().getName()));
            csvQuestion.setNumberOfPossibleAnswers(q.getPossibleAnswers().size());
            String[] possibleAnswers = new String[q.getPossibleAnswers().size()];
            q.getPossibleAnswers().toArray(possibleAnswers);
            csvQuestion.setPossibleAnswers(possibleAnswers);
            csvQuestion.setType(q.getType().toString());
            csvQuestions.add(csvQuestion);
            numberIterator++;
        }
        return csvQuestions;
    }

    public static void writeToCsvFile(Test test, String filePath) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        List<CsvQuestionModel> questionsToWrite = exportToCsv(test);

        Writer writer = new FileWriter(filePath);
        StatefulBeanToCsv<CsvQuestionModel> beanToCsv =
                new StatefulBeanToCsvBuilder<CsvQuestionModel>(writer)
                        .withSeparator(';')
                        .build();
        beanToCsv.write(questionsToWrite);
        writer.close();
    }

    private static String parseLanguageToCsv(String langugeName) {
        if (langugeName.equalsIgnoreCase("english") || langugeName.equalsIgnoreCase("en")) {
            return ENGLISH_LANG;
        } else if (langugeName.equalsIgnoreCase("polish") ||
                langugeName.equalsIgnoreCase("pl") || langugeName.equalsIgnoreCase("polski")) {
            return POLISH_LANG;
        }
        throw new RuntimeException("Couldn't parse String \"" + langugeName + "\" to any of the possible languages (EN or PL)");
    }
}
