package kr.co.won.user.controller;

import kr.co.won.auth.TestUser;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.factory.UserFactory;
import kr.co.won.user.persistence.UserPersistence;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Import(value = {UserFactory.class})
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserPersistence userPersistence;

    @Autowired
    private UserFactory userFactory;

    @AfterEach
    private void userDelete() {
        userPersistence.deleteAll();
    }


    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "01. create member Test - with ADMIN")
    @Test
    void createMemberWithADMINTests() throws Exception {
        mockMvc.perform(get("/admin/users/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("authUser"))
                .andExpect(model().attributeExists("createMemberForm"))
                .andExpect(view().name("admin/users/createMemberPage"));
    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "01. create member do Test - with ADMIN")
    @Test
    void createMemberDoWithADMINTests() throws Exception {

    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "02. find member Test - with ADMIN")
    @Test
    void findMemberPageWithADMINTests() throws Exception {

    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "03. list member test - with ADMIN")
    @Test
    void listMemberPageWithADMINTests() throws Exception {
        userFactory.bulkInsertTestUser(10, "testingUser", "test");
        mockMvc.perform(get("/admin/users/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("admin/users/listMemberPage"));
    }
}