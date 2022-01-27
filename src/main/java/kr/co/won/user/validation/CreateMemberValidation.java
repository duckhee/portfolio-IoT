package kr.co.won.user.validation;

import kr.co.won.user.form.CreateMemberForm;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CreateMemberValidation implements Validator {

    private final UserPersistence userPersistence;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateMemberForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateMemberForm form = (CreateMemberForm) target;
        // user email check
        if (userPersistence.existsByEmail(form.getEmail())) {
            errors.rejectValue("email", "already.email", null, "already have user email.");
        }
    }
}
