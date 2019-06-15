package com.masiblue.backend.translation;

import com.masiblue.backend.exception.LanguageTranslationNotSupportedException;
import com.masiblue.backend.model.Language;
import com.masiblue.backend.model.Test;

public interface Translator {
    Test translateTest(Test oldTest, Language desiredLanguage) throws LanguageTranslationNotSupportedException;
}
