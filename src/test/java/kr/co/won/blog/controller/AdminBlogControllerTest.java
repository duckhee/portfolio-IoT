package kr.co.won.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.TestUser;
import kr.co.won.blog.factory.BlogFactory;
import kr.co.won.blog.form.CreateBlogForm;
import kr.co.won.blog.persistence.BlogPersistence;
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
import org.springframework.ui.ModelMap;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
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
                .andExpect(model().attributeExists("createBlogForm"))
                .andExpect(view().name("admin/blogs/createBlogPage"));
    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "01. create blog Do Test - with POST(success)")
    @Test
    void createBlogDoWithADMIN_Tests() throws Exception {
        URL url = new URL("http://github.com/project/test");
        String content = "content";
        String title = "title";
        CreateBlogForm blogForm = CreateBlogForm.builder()
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
}