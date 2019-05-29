package com.masiblue.backend.utils;

import com.masiblue.backend.exception.InvalidCsvException;
import com.masiblue.backend.repository.TestRepository;
import com.masiblue.backend.service.ApplicationUserService;
import com.masiblue.backend.service.LanguageService;
import com.masiblue.backend.service.TestService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvReader {

    private static TestService testService;
    private static ApplicationUserService applicationUserService;
    private static LanguageService languageService;
    private static TestRepository testRepository;

    //    Numer;typ;język;treść;liczba możliwych odpowiedzi;[treść każdej odpowiedzi];

    // TODO: DO NOT READ FROM FILEPATH
    public static List<CsvQuestionModel> readCsv(String filePath) throws InvalidCsvException {
        List<CsvQuestionModel> questions = new ArrayList<>();

        try (
                Reader reader = Files.newBufferedReader(Paths.get(filePath));
        ) {
            CsvToBean<CsvQuestionModel> csvToBean = new CsvToBeanBuilder<CsvQuestionModel>(reader)
                    .withType(CsvQuestionModel.class)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            for (CsvQuestionModel csvQuestion : csvToBean) {
                System.out.println(csvQuestion.getNumber() + "\n" +
                        csvQuestion.getType() + "\n" +
                        csvQuestion.getLanguage() + "\n" +
                        csvQuestion.getContent() + "\n" +
                        csvQuestion.getNumberOfPossibleAnswers() + "\n" +
                        Arrays.toString(csvQuestion.getPossibleAnswers()));
                questions.add(csvQuestion);
                System.out.println("==========================");
            }
        } catch (Exception ex) {
            throw new InvalidCsvException();
        }
        return questions;
    }
}
