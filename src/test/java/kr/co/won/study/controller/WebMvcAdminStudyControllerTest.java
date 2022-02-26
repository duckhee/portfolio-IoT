package kr.co.won.study.controller;

import kr.co.won.auth.TestMockUser;
import kr.co.won.config.TestAppConfiguration;
import kr.co.won.config.TestWebMvcConfig;
import kr.co.won.config.WebSliceTest;
import kr.co.won.study.service.StudyService;
import kr.co.won.study.validation.CreateStudyValidation;
import kr.co.won.user.domain.UserRoleType;
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


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AdminStudyController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        SecurityConfig.class
                })
        })
// custom Web Slice Annotation
@WebSliceTest
/*
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(value = {MockitoExtension.class})
@Import(value = {TestAppConfiguration.class, TestWebMvcConfig.class})
 */
class WebMvcAdminStudyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateStudyValidation createStudyValidation;

    @MockBean
    private StudyService studyService;

    @DisplayName(value = "01. Admin Study Create Page Web Slice Tests")
    @Test
    void studyCreatePageTests() throws Exception {
        mockMvc.perform(get("/admin/study/create"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @TestMockUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "01. Admin Study Create Page Web Slice Tests - with ADMIN")
    @Test
    void studyCreatePageTests_withADMIN() throws Exception {
        when(createStudyValidation.supports(any())).thenReturn(true);
        mockMvc.perform(get("/admin/study/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("createStudyForm"))
                .andExpect(view().name("admin/study/studyCreatePage"));
    }
}