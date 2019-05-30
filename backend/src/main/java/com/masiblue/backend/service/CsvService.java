package com.masiblue.backend.service;

import com.masiblue.backend.exception.InvalidCsvException;
import com.masiblue.backend.model.CsvQuestionModel;
import com.masiblue.backend.model.Question;
import com.masiblue.backend.model.Test;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    private static final String ENGLISH_LANG = "EN";
    private static final String POLISH_LANG = "PL";

    //    Numer;typ;język;treść;liczba możliwych odpowiedzi;[treść każdej odpowiedzi];

    List<CsvQuestionModel> readCsvQuestions(MultipartFile file) throws InvalidCsvException {
        List<CsvQuestionModel> questions = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Reader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            CsvToBean<CsvQuestionModel> csvToBean = new CsvToBeanBuilder<CsvQuestionModel>(reader)
                    .withType(CsvQuestionModel.class)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            csvToBean.forEach(questions::add);

        } catch (Exception ex) {
            throw new InvalidCsvException();
        }
        return questions;
    }

    private List<CsvQuestionModel> exportToCsv(Test test) {
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

    public void writeToCsvFile(Test test, String filePath) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        List<CsvQuestionModel> questionsToWrite = exportToCsv(test);

        Writer writer = new FileWriter(filePath);
        StatefulBeanToCsv<CsvQuestionModel> beanToCsv =
                new StatefulBeanToCsvBuilder<CsvQuestionModel>(writer)
                        .withSeparator(';')
                        .build();
        beanToCsv.write(questionsToWrite);
        writer.close();
    }

    private String parseLanguageToCsv(String langugeName) {
        if (langugeName.equalsIgnoreCase("english") || langugeName.equalsIgnoreCase("en")) {
            return ENGLISH_LANG;
        } else if (langugeName.equalsIgnoreCase("polish") ||
                langugeName.equalsIgnoreCase("pl") || langugeName.equalsIgnoreCase("polski")) {
            return POLISH_LANG;
        }
        throw new RuntimeException("Couldn't parse String \"" + langugeName + "\" to any of the possible languages (EN or PL)");
    }

}
