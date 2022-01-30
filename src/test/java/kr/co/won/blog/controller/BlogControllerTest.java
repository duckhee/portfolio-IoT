package kr.co.won.blog.controller;

import kr.co.won.auth.TestUser;
import kr.co.won.blog.form.CreateBlogForm;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserPersistence userPersistence;

    @Autowired
    private BlogPersistence blogPersistence;

    @AfterEach
    void testDataSet() {
        userPersistence.deleteAll();
        blogPersistence.deleteAll();
    }

    @DisplayName(value = "01. create blog Test - with GET")
    @Test
    void createBlogPageTests() throws Exception {
        mockMvc.perform(get("/blogs/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("blogs/createBlogPage"))
                .andExpect(model().attributeExists("createBlogForm"));
    }

    @TestUser(authLevel = UserRoleType.USER)
    @DisplayName(value = "01. creat blog do Test - with POST(Success)")
    @Test
    void createBlogDoSuccessTests() throws Exception {
        String title = "title";
        String cont = "blog content";
        mockMvc.perform(post("/blogs/create")
                        .with(csrf())
                        .param("title", title)
                        .param("content", cont))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blogs/list"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(view().name("redirect:/blogs/list"));
    }

    @DisplayName(value = "01. creat blog do Test - with POST(Not Login)")
    @Test
    void createBlogDoFailedNotLoginUserTests() throws Exception {
        String title = "title";
        String cont = "blog content";
        mockMvc.perform(post("/blogs/create")
                        .with(csrf())
                        .param("title", title)
                        .param("content", cont))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(view().name("redirect:/login"));
    }
}