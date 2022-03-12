package kr.co.won.study.validation;

import kr.co.won.study.domain.StudyStatusType;
import kr.co.won.study.form.UpdateStudyForm;
import kr.co.won.study.persistence.StudyPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UpdateStudyValidation implements Validator {

    private final StudyPersistence studyPersistence;
    private final MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateStudyForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UpdateStudyForm form = (UpdateStudyForm) target;
        Locale local = LocaleContextHolder.getLocale();
        if (studyPersistence.existsByPath(form.getPath())) {
            errors.rejectValue("path", "duplicated.path", messageSource.getMessage("duplicated.path", null, "already have path. try to another path.", local));
        }
        // recruiting time now before and same
        if (form.getStatus() != null) {
            if (form.getStatus().equals(StudyStatusType.RECRUIT) && (LocalDateTime.now().equals(form.getStatusTime()) || LocalDateTime.now().isBefore(form.getStatusTime()))) {
                errors.rejectValue("statusTime", "wrong.statusTime", messageSource.getMessage("wrong.statusTime", null, "wrong recruiting time.", local));
            }
        }

    }
}
