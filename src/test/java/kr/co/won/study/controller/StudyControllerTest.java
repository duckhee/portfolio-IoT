package kr.co.won.study.controller;

import kr.co.won.auth.TestUser;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserPersistence userPersistence;

    @AfterEach
    public void InitData() {
        userPersistence.deleteAll();
    }

    @TestUser(authLevel = UserRoleType.USER)
    @DisplayName(value = "01. create sutdy Page - with GET(success)")
    @Test
    void studyCreatePageTests_withUSER() throws Exception {
        mockMvc.perform(get("/study/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/createPage"))
                .andExpect(model().attributeExists("user", "createStudyForm"));
    }

}
