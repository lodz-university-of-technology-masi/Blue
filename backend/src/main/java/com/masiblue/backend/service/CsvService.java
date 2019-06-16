package com.masiblue.backend.service;

import com.masiblue.backend.exception.InvalidCsvException;
import com.masiblue.backend.model.CsvQuestionModel;
import com.masiblue.backend.model.Question;
import com.masiblue.backend.model.Test;
import com.masiblue.backend.model.Type;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    private static final String ENGLISH_LANG = "EN";
    private static final String POLISH_LANG = "PL";
    private static final String CSV_MEDIA_TYPE = "text/csv";
    private static final String CSV_SEPARATOR = ";";
    private static final String HEADER = "Content-Disposition";
    private static final String HEADER_VALUE = "attachment; filename=test.csv";

    //    Numer;typ;język;treść;liczba możliwych odpowiedzi;[treść każdej odpowiedzi];

    List<CsvQuestionModel> readCsvQuestions(MultipartFile file) throws InvalidCsvException {
        List<CsvQuestionModel> questions = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line = reader.readLine();
            while (line != null) {
                CsvQuestionModel csvQuestion = new CsvQuestionModel();
                String[] split = line.split(CSV_SEPARATOR);

                csvQuestion.setNumber(Integer.parseInt(split[0]));
                csvQuestion.setType(split[1]);
                csvQuestion.setLanguage(split[2]);
                csvQuestion.setContent(split[3]);
                if (csvQuestion.getType().equals("W") || csvQuestion.getType().equals("S")) {
                    csvQuestion.setNumberOfPossibleAnswers(Integer.parseInt(split[4]));
                    csvQuestion.setPossibleAnswers(split[5]);
                } else {
                    csvQuestion.setPossibleAnswers(split[4]);
                }
                questions.add(csvQuestion);
                line = reader.readLine();
            }

        } catch (Exception ex) {
            throw new InvalidCsvException(ex.getMessage());
        }
        return questions;
    }

    private List<CsvQuestionModel> exportToCsvQuestions(Test test) {
        List<Question> questions = test.getQuestions();
        List<CsvQuestionModel> csvQuestions = new ArrayList<>();
        int numberIterator = 1;
        for (Question q : questions) {
            CsvQuestionModel csvQuestion = new CsvQuestionModel();
            csvQuestion.setNumber(numberIterator);
            csvQuestion.setContent(q.getContent());
            csvQuestion.setLanguage(parseLanguageToCsv(test.getLanguage().getName()));
            csvQuestion.setNumberOfPossibleAnswers(q.getPossibleAnswers().size());
            csvQuestion.setType(q.getType().toString());
            if (q.getType().equals(Type.O) || q.getType().equals(Type.L)) {
                csvQuestion.setPossibleAnswers("|");
            } else {
                csvQuestion.setPossibleAnswers(String.join("|", q.getPossibleAnswers()));
            }
            csvQuestions.add(csvQuestion);
            numberIterator++;
        }
        return csvQuestions;
    }

    void exportToCsvFile(Test test, HttpServletResponse response) throws IOException {
        List<CsvQuestionModel> questionsToWrite = exportToCsvQuestions(test);
        response.setContentType(CSV_MEDIA_TYPE);
        response.setHeader(HEADER, HEADER_VALUE);

        try (PrintWriter pw = response.getWriter()) {
            for (CsvQuestionModel q : questionsToWrite) {
                String question = "" + q.getNumber() + CSV_SEPARATOR +
                        q.getType() + CSV_SEPARATOR +
                        q.getLanguage() + CSV_SEPARATOR +
                        q.getContent() + CSV_SEPARATOR;
                if (q.getNumberOfPossibleAnswers() != 0) {
                    question += ("" + q.getNumberOfPossibleAnswers() + CSV_SEPARATOR);
                }
                question += q.getPossibleAnswers() + CSV_SEPARATOR + "\n";
                pw.write(question);
            }
        }
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
