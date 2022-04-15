package kr.co.won.iot.validation;

import kr.co.won.iot.form.CreateAdminSiteForm;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AdminCreateSiteValidator implements Validator {

    /**
     * user persistence check user email
     */
    private final UserPersistence userPersistence;

    /**
     * message source
     */
    private final MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateAdminSiteForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // get controller form
        CreateAdminSiteForm form = (CreateAdminSiteForm) target;
        String userEmail = form.getUserEmail();
        // get locale
        Locale locale = LocaleContextHolder.getLocale();
        // user active flag and delete flag check by user email
        if (!userPersistence.existsByEmailAndActiveFlagFalseAndDeleteFlagFalse(userEmail)) {
            errors.rejectValue("userEmail", "not.have.user", messageSource.getMessage("not.have.user", null, "not have user. or not active user.", locale));
        }

    }
}
