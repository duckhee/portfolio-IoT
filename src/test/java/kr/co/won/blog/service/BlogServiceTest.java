package kr.co.won.blog.service;

import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.blog.persistence.BlogReplyPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.factory.UserDomainBuilderFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.net.URI;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(value = {MockitoExtension.class})
@Import(value = {UserDomainBuilderFactory.class})
class BlogServiceTest {

    private ModelMapper modelMapper = new ModelMapper();

    private UserDomainBuilderFactory userDomainBuilder = new UserDomainBuilderFactory();

    @Mock
    private BlogPersistence blogPersistence;

    @Mock
    private BlogReplyPersistence blogReplyPersistence;

    @DisplayName(value = "01. create blog Test")
    @Test
    void createBlogTests() {
        BlogService blogService = new BlogServiceImpl(modelMapper, blogPersistence, blogReplyPersistence);

        String title = "test";
        String content = "testContent";
        String writer = "writer";
        String email = "writer@co.kr";
        String projectUri = URI.create("/github.com/test/blog").toString();
        BlogDomain testBlog = blogBuilder(title, content, writer, email, projectUri);
        // user persistence return setting
        when(blogPersistence.save(testBlog)).thenReturn(stubBlog(testBlog));

        BlogDomain serviceBlog = blogService.createBlog(testBlog, email, writer);

        assertThat(serviceBlog.getIdx()).isNotNull();
        assertThat(serviceBlog.getTitle()).isEqualTo(testBlog.getTitle());
        assertThat(serviceBlog.getContent()).isEqualTo(testBlog.getContent());
        assertThat(serviceBlog.getWriterEmail()).isEqualTo(testBlog.getWriterEmail());
        assertThat(serviceBlog.getWriter()).isEqualTo(testBlog.getWriter());

    }

    @DisplayName(value = "01. create blog Test - with user")
    @Test
    void createBlogWithUserTests() {
        BlogService blogService = new BlogServiceImpl(modelMapper, blogPersistence, blogReplyPersistence);

        String title = "test";
        String content = "testContent";
        String name = "tester";
        String email = "tester@co.kr";
        String password = "1234";
        String projectUri = URI.create("/github.com/test/blog").toString();
        UserDomain testUser = userDomainBuilder.makeDomainUser(1L, name, email, password, UserRoleType.USER);
        BlogDomain testBlog = blogBuilder(title, content,  projectUri);
        // stubbing save
        when(blogPersistence.save(testBlog)).thenReturn(stubBlog(testBlog));
        //
        BlogDomain testBlogs = blogService.createBlog(testBlog, testUser);
        assertThat(testBlogs.getIdx()).isNotNull();
        assertThat(testBlogs.getTitle()).isEqualTo(testBlog.getTitle());
        assertThat(testBlogs.getContent()).isEqualTo(testBlog.getContent());
        assertThat(testBlogs.getWriterEmail()).isEqualTo(testUser.getEmail());
        assertThat(testBlogs.getWriter()).isEqualTo(testUser.getName());

    }



    // blog builder
    private BlogDomain blogBuilder(String test, String testContent, String writer, String email, String projectUri) {
        BlogDomain testBlog = BlogDomain.builder()
                .title(test)
                .content(testContent)
                .writer(writer)
                .writerEmail(email)
                .projectUri(projectUri)
                .build();
        return testBlog;
    }

    private BlogDomain blogBuilder(String title, String projectUri, String content) {
        BlogDomain builder = BlogDomain.builder()
                .title(title)
                .content(content)
                .projectUri(projectUri)
                .build();
        return builder;
    }

    // stubbing blog
    private BlogDomain stubBlog(BlogDomain blog) {
        blog.setIdx(1L);
        blog.setCreatedAt(LocalDateTime.now());
        blog.setUpdatedAt(LocalDateTime.now());
        return blog;
    }


}