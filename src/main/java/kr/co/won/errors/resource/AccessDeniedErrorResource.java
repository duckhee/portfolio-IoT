package kr.co.won.errors.resource;

import kr.co.won.errors.resource.dto.ErrorDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.AccessDeniedException;

public class AccessDeniedErrorResource extends EntityModel<ErrorDto> {

    public static EntityModel<ErrorDto> modelOf(ErrorDto exception) {

        EntityModel<ErrorDto> returnResource = EntityModel.of(exception);
        return returnResource;
    }
}
