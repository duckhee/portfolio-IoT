package kr.co.won.blog.api.assembler;

import kr.co.won.blog.api.BlogApiController;
import kr.co.won.blog.api.resource.dto.BlogReadResourcesDto;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BlogAssembler implements RepresentationModelAssembler<BlogDomain, BlogReadResourcesDto> {

    private final ModelMapper modelMapper;

    @Override
    public BlogReadResourcesDto toModel(BlogDomain entity) {
        BlogReadResourcesDto mappedBlog = new BlogReadResourcesDto(entity);
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(BlogApiController.class);
        basicLinkAdd(entity, mappedBlog, linkBuilder);
        return mappedBlog;
    }


    public BlogReadResourcesDto toModel(BlogDomain entity, UserDomain user) {
        BlogReadResourcesDto mappedBlog = new BlogReadResourcesDto(entity);
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(BlogApiController.class);
        if (isAuth(entity, user)) {
            mappedBlog.add(linkBuilder.slash(entity.getIdx()).withRel("update-blogs").withType(HttpMethod.PUT.name()));
            mappedBlog.add(linkBuilder.slash(entity.getIdx()).withRel("delete-blogs").withType(HttpMethod.DELETE.name()));
        }
        mappedBlog.add(linkBuilder.slash(entity.getIdx()).withRel("query-blogs").withType(HttpMethod.GET.name()));

        return mappedBlog;
    }


    @Override
    public CollectionModel<BlogReadResourcesDto> toCollectionModel(Iterable<? extends BlogDomain> entities) {
        List<BlogReadResourcesDto> list = new ArrayList<>();
        entities.forEach(entity -> list.add(this.toModel(entity)));
        return CollectionModel.of(list);
    }

    public CollectionModel<BlogReadResourcesDto> toCollectionModel(Iterable<? extends BlogDomain> entities, UserDomain user) {
        List<BlogReadResourcesDto> list = new ArrayList<>();
        entities.forEach(entity -> list.add(this.toModel(entity, user)));
        return CollectionModel.of(list);
    }

    // user role check
    private boolean isAuth(BlogDomain entity, UserDomain user) {
        return user.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER) || user.getEmail().equals(entity.getWriterEmail());
    }

    // add base link
    private void basicLinkAdd(BlogDomain entity, BlogReadResourcesDto mappedBlog, WebMvcLinkBuilder linkBuilder) {
        mappedBlog.add(linkBuilder.slash(entity.getIdx()).withRel("query-blogs").withType(HttpMethod.GET.name()));
        mappedBlog.add(linkBuilder.slash(entity.getIdx()).withRel("update-blogs").withType(HttpMethod.PUT.name()));
        mappedBlog.add(linkBuilder.slash(entity.getIdx()).withRel("delete-blogs").withType(HttpMethod.DELETE.name()));
    }
}
