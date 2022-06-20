package kr.co.won.iot.api.resource.dto;

import kr.co.won.iot.api.IoTApiController;
import kr.co.won.iot.domain.SiteDomain;
import kr.co.won.user.domain.UserDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;


/**
 * IoTCreateResourceDto
 * Version:V0.0.1
 * author: Douckhee Won
 * DATE: 2022/06/20
 * <p>
 * DESCRIPTION
 * Site Create Done Return Dto
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IoTCreateResourceDto extends RepresentationModel<IoTCreateResourceDto> {

    // site primary key
    private Long idx;

    // site name
    private String siteName;

    // site create time
    private LocalDateTime createdAt;


    public IoTCreateResourceDto(SiteDomain siteDomain, UserDomain authUser) {
        this.idx = siteDomain.getIdx();
        this.siteName = siteDomain.getName();
        this.createdAt = siteDomain.getCreatedAt();
//        this.add(WebMvcLinkBuilder.linkTo(IoTApiController.class).slash(this.idx).withRel("get-IoT-site").withType(HttpMethod.GET.name()));
    }


}
