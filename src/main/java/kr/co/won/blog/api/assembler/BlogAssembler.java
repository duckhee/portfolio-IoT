package kr.co.won.blog.api.assembler;

import kr.co.won.blog.api.BlogApiController;
import kr.co.won.blog.api.resource.dto.BlogReadResourcesDto;
import kr.co.won.blog.domain.BlogDomain;
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
        mappedBlog.add(linkBuilder.slash(entity.getIdx()).withRel("query-blogs").withType(HttpMethod.GET.name()));
        mappedBlog.add(linkBuilder.slash(entity.getIdx()).withRel("update-blogs").withType(HttpMethod.PUT.name()));
        mappedBlog.add(linkBuilder.slash(entity.getIdx()).withRel("delete-blogs").withType(HttpMethod.DELETE.name()));
        return mappedBlog;
    }

    @Override
    public CollectionModel<BlogReadResourcesDto> toCollectionModel(Iterable<? extends BlogDomain> entities) {
        List<BlogReadResourcesDto> list = new ArrayList<>();
        entities.forEach(entity -> list.add(this.toModel(entity)));
        return CollectionModel.of(list);
    }
}
