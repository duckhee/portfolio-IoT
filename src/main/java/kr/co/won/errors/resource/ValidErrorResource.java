package kr.co.won.errors.resource;


import kr.co.won.main.MainApiController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.validation.Errors;


public class ValidErrorResource extends EntityModel<Errors> {

    public static EntityModel<Errors> modelOf(Errors errors) {
        /** valid error move index link */
        EntityModel<Errors> validErrorModel = EntityModel.of(errors);
        /** main index api url setting link */
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MainApiController.class).indexApiInformation());
        validErrorModel.add(linkBuilder.withRel("index"));
        return validErrorModel;
    }
}
