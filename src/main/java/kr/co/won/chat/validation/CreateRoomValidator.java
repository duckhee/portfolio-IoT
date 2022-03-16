package kr.co.won.chat.validation;

import kr.co.won.chat.domain.RoomType;
import kr.co.won.chat.form.CreateChattingRoomForm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CreateRoomValidator implements Validator {

    private final MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateChattingRoomForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateChattingRoomForm form = (CreateChattingRoomForm) target;
        // get locale
        Locale locale = LocaleContextHolder.getLocale();
        // chatting room type is private
        if (form.getType() != null && (form.getType().equals(RoomType.PRIVATE) && form.getPassword() == null)) {
            //errors.rejectValue("password", "need.password", null, messageSource.getMessage("need.password", null, locale));
            errors.rejectValue("password", "need.password", null, "this chatting room type need to password");
        }
    }
}
