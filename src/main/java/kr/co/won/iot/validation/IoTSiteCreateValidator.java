package kr.co.won.iot.validation;


import kr.co.won.iot.form.CreateSiteForm;
import kr.co.won.iot.persistence.SitePersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@RequiredArgsConstructor
public class IoTSiteCreateValidator implements Validator {

    /**
     * database access Repository
     */
    private final SitePersistence sitePersistence;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateSiteForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateSiteForm form = (CreateSiteForm) target;
    }
}
