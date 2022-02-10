package kr.co.won.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.TestUser;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.factory.BlogFactory;
import kr.co.won.blog.form.BlogForm;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.factory.UserFactory;
import kr.co.won.user.persistence.UserPersistence;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Import(value = {UserFactory.class, BlogFactory.class})
class AdminBlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserPersistence userPersistence;

    @Autowired
    private BlogPersistence blogPersistence;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private BlogFactory blogFactory;

    @AfterEach
    public void testDataInit() {
        userPersistence.deleteAll();
        blogPersistence.deleteAll();
    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "01. creat blogs Page Test - with GET(success)")
    @Test
    void createBlogPageWithADMIN_Tests() throws Exception {
        mockMvc.perform(get("/admin/blogs/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("blogForm"))
                .andExpect(view().name("admin/blogs/createBlogPage"));
    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "01. create blog Do Test - with POST(success)")
    @Test
    void createBlogDoWithADMIN_Tests() throws Exception {
        URL url = new URL("http://github.com/project/test");
        String content = "content";
        String title = "title";
        BlogForm blogForm = BlogForm.builder()
                .title(title)
                .content(content)
                .projectUrl(url)
                .build();
        mockMvc.perform(post("/admin/blogs/create")
                        .param("title", title)
                        .param("content", content)
                        .param("url", url.toString())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/blogs/list"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(view().name("redirect:/admin/blogs/list"));
    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "02. blog list Page Test - with GET(success)")
    @Test
    void listBlogPageWithADMIN_Tests() throws Exception {
        mockMvc.perform(get("/admin/blogs/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("page"))
                .andExpect(view().name("admin/blogs/listBlogPage"));
    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "03. blog read page Test -with GET(success)")
    @Test
    void readBlogPageWithADMIN_Tests() throws Exception {
        UserDomain testUser = userFactory.testUser("tester1", "tester1@co.kr", "1234");
        BlogDomain testBlog = blogFactory.makeBlogWithReply("testing", "testing", testUser, 10);
        mockMvc.perform(get("/admin/blogs/"+testBlog.getIdx()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("blog"))
                .andExpect(model().attributeExists("replies"))
                .andExpect(view().name("admin/blogs/informationBlogPage"));
    }
}