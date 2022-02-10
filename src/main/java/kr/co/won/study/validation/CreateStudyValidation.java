package kr.co.won.study.validation;

import groovyjarjarpicocli.CommandLine;
import kr.co.won.study.form.CreateStudyForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CreateStudyValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateStudyForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateStudyForm formData = (CreateStudyForm) target;

    }
}
