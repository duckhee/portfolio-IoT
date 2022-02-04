package kr.co.won.blog.api.resource.dto;

import kr.co.won.blog.domain.BlogDomain;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.CollectionModel;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BlogCollectResourcesDto extends CollectionModel<BlogReadResourcesDto> {

    private List<BlogReadResourcesDto> blogs;

    public BlogCollectResourcesDto(List<BlogDomain> blogs) {
        List<BlogReadResourcesDto> collectBlog = blogs.stream().map(blog -> new BlogReadResourcesDto(blog)).collect(Collectors.toList());
        this.blogs = collectBlog;
    }



}
