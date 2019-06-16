package com.masiblue.backend.translation;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.masiblue.backend.exception.LanguageTranslationNotSupportedException;
import com.masiblue.backend.model.Language;
import com.masiblue.backend.model.Question;
import com.masiblue.backend.model.Test;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GoogleTranslator implements Translator {

    @Override
    public Test translateTest(Test oldTest, Language desiredLanguage) throws LanguageTranslationNotSupportedException {
        if(!languageCodes.containsValue(desiredLanguage.getName())) {
            if(!languageCodes.containsKey(desiredLanguage.getName())) {
                throw new LanguageTranslationNotSupportedException();
            } else {
                targetLanguageCode = languageCodes.get(desiredLanguage.getName());
            }
        } else {
            throw new LanguageTranslationNotSupportedException();
        }
        sourceLanguageCode = languageCodes.get(oldTest.getLanguage().getName());

        Test newTest = copyTestData(oldTest, desiredLanguage);

        List<Question> translatedQuestions = new ArrayList<>();

        for (Question oldQuestion : oldTest.getQuestions()) {
            Question newQuestion = translateQuestion(oldQuestion);
            newQuestion.setTest(newTest);
            translatedQuestions.add(newQuestion);
        }

        newTest.setQuestions(translatedQuestions);
        return newTest;
    }

    private Question translateQuestion(Question oldQuestion) {

        Translation contentTranslation = translate.translate(oldQuestion.getContent(),
                Translate.TranslateOption.sourceLanguage(sourceLanguageCode),
                Translate.TranslateOption.targetLanguage(targetLanguageCode),
                Translate.TranslateOption.format("text"));

        Question newQuestion = new Question();
        newQuestion.setType(oldQuestion.getType());
        newQuestion.setContent(contentTranslation.getTranslatedText());

        List<String> answersList = new ArrayList<>(oldQuestion.getPossibleAnswers());

        if(!answersList.isEmpty()) {
            List<Translation> answersTranslation = translate.translate(answersList,
                    Translate.TranslateOption.sourceLanguage(sourceLanguageCode),
                    Translate.TranslateOption.targetLanguage(targetLanguageCode));

            Set<String> translatedAnswers = answersTranslation.stream()
                    .map(Translation::getTranslatedText).collect(Collectors.toSet());
            newQuestion.setPossibleAnswers(translatedAnswers);
        }

        return newQuestion;
    }

    private Test copyTestData(Test oldTest, Language language) {
        Test newTest = new Test();
        newTest.setPosition(oldTest.getPosition());
        newTest.setLanguage(language);
        newTest.setAuthor(oldTest.getAuthor());
        newTest.setName(oldTest.getName());
        newTest.setCreationDate(LocalDateTime.now());
        return newTest;
    }

    private final Translate translate = TranslateOptions.getDefaultInstance().getService();
    private String sourceLanguageCode;
    private String targetLanguageCode;

    private static HashMap<String, String> languageCodes;

    static {
        languageCodes = new HashMap<>();
        languageCodes.put("Polski", "pl");
        languageCodes.put("English", "en");
        languageCodes.put("PL", "pl");
        languageCodes.put("EN", "en");
    }

}
