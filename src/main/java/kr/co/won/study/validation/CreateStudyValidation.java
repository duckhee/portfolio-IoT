package kr.co.won.study.validation;

import groovyjarjarpicocli.CommandLine;
import kr.co.won.study.form.CreateStudyForm;
import kr.co.won.study.persistence.StudyPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CreateStudyValidation implements Validator {

    private final StudyPersistence studyPersistence;
    private final MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateStudyForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateStudyForm formData = (CreateStudyForm) target;
        Locale locale = LocaleContextHolder.getLocale();
        // study path check
        if (studyPersistence.existsByPath(formData.getPath())) {
            errors.rejectValue("path", "invalid.path", messageSource.getMessage("invalid.path", null, "already have path", locale));
        }

    }
}
