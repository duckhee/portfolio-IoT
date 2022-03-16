package kr.co.won.user.controller;

import kr.co.won.auth.TestMockUser;
import kr.co.won.config.AppConfiguration;
import kr.co.won.config.TestAppConfiguration;
import kr.co.won.config.TestWebMvcConfig;
import kr.co.won.config.datasources.DataSourceConfiguration;
import kr.co.won.config.datasources.RedisConfiguration;
import kr.co.won.config.security.ApiSecurityConfiguration;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.service.UserService;
import kr.co.won.user.validation.CreateMemberValidation;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AdminUserController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        SecurityConfig.class,
                        ApiSecurityConfiguration.class
                }),
        },
        excludeAutoConfiguration = {
                AppConfiguration.class,
                DataSourceConfiguration.class,
                RedisConfiguration.class
        })
@AutoConfigureMockMvc
@ExtendWith(value = {MockitoExtension.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(value = {TestAppConfiguration.class, TestWebMvcConfig.class})
class WebMvcAdminUserControllerTest {


    @MockBean(name = "adminUserService")
    private UserService userService;
    @MockBean
    private CreateMemberValidation createMemberValidation;
    @Autowired
    private MockMvc mockMvc;

    @DisplayName(value = "01. web MVC Test Admin User Controller Create Page Test - with GET")
    @Test
    void adminUserCreatePageTests() throws Exception {
        mockMvc.perform(get("/admin/users/create"))
                .andExpect(status().is3xxRedirection());
    }

    @TestMockUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "01. web MVC Test Admin User Controller Create Page Test - with GET")
    @Test
    void adminUserCreatePageTests_withADMIN() throws Exception {
        when(createMemberValidation.supports(any())).thenReturn(true);
        mockMvc.perform(get("/admin/users/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("createMemberForm", "authUser"))
                .andExpect(view().name("admin/users/createMemberPage"));
    }

}