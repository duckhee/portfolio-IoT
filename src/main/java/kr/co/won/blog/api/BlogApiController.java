package kr.co.won.blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.blog.api.assembler.BlogAssembler;
import kr.co.won.blog.api.resource.BlogCreateResource;
import kr.co.won.blog.api.resource.dto.*;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.form.CreateBlogForm;
import kr.co.won.blog.service.BlogService;
import kr.co.won.errors.resource.ValidErrorResource;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.PagedModel.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
@RequestMapping(path = "/api/blogs")
@RequiredArgsConstructor
public class BlogApiController {

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    /**
     * blog service
     */
    private final BlogService blogService;

    private final PagedResourcesAssembler pagedResourcesAssembler;
    private final BlogAssembler blogAssembler;

    @GetMapping
    public ResponseEntity listBlogResources(PageDto pageDto) {
        Page pageList = blogService.pagingBlog(pageDto);
        PagedModel resultResource = pagedResourcesAssembler.toModel(pageList, blogAssembler);
        PageMetadata pageMetadata = new PageMetadata(pageDto.getSize(), pageList.getNumber(), pageList.getTotalElements(), pageList.getTotalPages());

        /** Paging */
        List<BlogDomain> content = pageList.getContent();
        List<BlogReadResourcesDto> collect = content.stream().map(blog -> new BlogReadResourcesDto(blog)).collect(Collectors.toList());
        PagedModel<BlogReadResourcesDto> result = of(collect, pageMetadata);
        // webLink Base
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(BlogApiController.class);
        resultResource.add(Link.of("/docs/index.html#blog-list-resources", "profile").withType(HttpMethod.GET.name()));
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity createBlogResources(@AuthUser UserDomain loginUser, @Validated @RequestBody CreateBlogForm form, Errors errors) {
        if (loginUser == null) {
            throw new AccessDeniedException("Not Login.");
        }
        if (errors.hasErrors()) {
            return validationResources(errors);
        }
        BlogDomain newBlog = modelMapper.map(form, BlogDomain.class);
        BlogDomain savedBlog = blogService.createBlog(newBlog, loginUser);
        // mapped BlogResources
        BlogCreateResourceDto mappedNewBlog = modelMapper.map(savedBlog, BlogCreateResourceDto.class);
        // blog resource
        EntityModel<BlogCreateResourceDto> blogResource = BlogCreateResource.of(mappedNewBlog);
        // base hateoas controller link
        WebMvcLinkBuilder baseLink = linkTo(BlogApiController.class);
        // make creat uri
        URI createUri = baseLink.slash(savedBlog.getIdx()).toUri();
        // add blog hateoas link
        blogResource.add(baseLink.withRel("list-blogs").withType(HttpMethod.GET.name()));

        blogResource.add(baseLink.slash(savedBlog.getIdx()).withRel("query-blogs").withType(HttpMethod.GET.name()));
        blogResource.add(baseLink.slash(savedBlog.getIdx()).withRel("update-blogs").withType(HttpMethod.PUT.name()));
        blogResource.add(baseLink.slash(savedBlog.getIdx()).withRel("delete-blogs").withType(HttpMethod.DELETE.name()));
        blogResource.add(Link.of("/docs/index.html#blog-create-resources", "profile").withType(HttpMethod.GET.name()));
        return ResponseEntity.created(createUri).body(blogResource);
    }

    @GetMapping(path = "/{idx}")
    public ResponseEntity findBlogResources(@PathVariable(value = "idx") Long idx) {

        BlogDomain findBlog = blogService.readBlog(idx);
        BlogReadResourcesDto blogReadResourcesDto = new BlogReadResourcesDto(findBlog);
        // add hal links
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(BlogApiController.class);
        blogReadResourcesDto.add(linkBuilder.withRel("list-blogs").withType(HttpMethod.GET.name()));
        // login user check and writer check
        blogReadResourcesDto.add(linkBuilder.withRel("create-blogs").withType(HttpMethod.POST.name()));
        blogReadResourcesDto.add(linkBuilder.withRel("update-blogs").withType(HttpMethod.PUT.name()));
        blogReadResourcesDto.add(linkBuilder.withRel("delete-blogs").withType(HttpMethod.DELETE.name()));
        blogReadResourcesDto.add(Link.of("/docs/index.html#blog-read-resources", "profile").withType(HttpMethod.GET.name()));
        return ResponseEntity.ok().body(blogReadResourcesDto);
    }

    @PutMapping(path = "/{idx}")
    public ResponseEntity updateBlogPutResource(@PathVariable(name = "idx") Long idx, @AuthUser UserDomain loginUser) {
        return null;
    }

    @PatchMapping(path = "/{idx}")
    public ResponseEntity updateBlogResource(@PathVariable(value = "idx") Long blogIdx, @AuthUser UserDomain loginUser) {
        return null;
    }

    private ResponseEntity validationResources(Errors errors) {
        EntityModel<Errors> validationErrorResource = ValidErrorResource.of(errors);
        return ResponseEntity.badRequest().body(validationErrorResource);
    }

}
