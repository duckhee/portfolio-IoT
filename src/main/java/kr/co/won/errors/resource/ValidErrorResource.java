package kr.co.won.errors.resource;


import org.springframework.hateoas.EntityModel;
import org.springframework.validation.Errors;


public class ValidErrorResource extends EntityModel<Errors> {

    public static EntityModel<Errors> modelOf(Errors errors) {
        /** valid error move index link */
        EntityModel<Errors> validErrorModel = EntityModel.of(errors);
        return validErrorModel;
    }
}
