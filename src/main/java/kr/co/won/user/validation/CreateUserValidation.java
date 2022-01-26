package kr.co.won.user.validation;

import kr.co.won.user.form.CreateUserForm;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CreateUserValidation implements Validator {

    private final MessageSource messageSource;

    private final UserPersistence userPersistence;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateUserForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // get user create form
        CreateUserForm form = (CreateUserForm) target;
        // password matching validation
        if (!form.getPassword().equals(form.getConfirmPassword())) {
//            errors.rejectValue("password", "not.match.password", null, messageSource.getMessage("not.match.password", null, LocaleContextHolder.getLocale()));
            errors.rejectValue("password", "not.match.password", null, "password not match confirm password");
        }
        // user email validation
        if (userPersistence.existsByEmail(form.getEmail())) {
//            errors.rejectValue("email", "already.email", null, messageSource.getMessage("already.email", null, LocaleContextHolder.getLocale()));
            errors.rejectValue("email", "already.email", null, "user email already regsite.");
        }
        //
    }
}
