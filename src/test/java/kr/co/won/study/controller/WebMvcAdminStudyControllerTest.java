package kr.co.won.study.controller;

import kr.co.won.auth.LoginUser;
import kr.co.won.auth.TestMockUser;
import kr.co.won.config.WebSliceTest;
import kr.co.won.config.security.ApiSecurityConfiguration;
import kr.co.won.config.security.SecurityConfiguration;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.form.CreateStudyForm;
import kr.co.won.study.service.StudyService;
import kr.co.won.study.validation.CreateStudyValidation;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.type.UserRoleType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;


import javax.annotation.Resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(controllers = {AdminStudyController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        SecurityConfiguration.class,
                        ApiSecurityConfiguration.class
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

    @Autowired
    private MessageSource messageSource;


    @Spy
    @Resource(name = "skipModelMapper")
    private ModelMapper modelMapper;

    @Spy
    @Resource(name = "notSkipModelMapper")
    private ModelMapper notSkipModelMapper;

    @MockBean
    private CreateStudyValidation createStudyValidation;

    @MockBean
    private StudyService studyService;


    @InjectMocks
    private AdminStudyController adminStudyController;


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


    @TestMockUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "02. Admin Study Create Do Web Slice Tests - with ADMIN")
    @Test
    void studyCreateDoTests_withADMIN() throws Exception {
        // validation true
        when(createStudyValidation.supports(any())).thenReturn(true);

        CreateStudyForm testStudy = CreateStudyForm.builder()
                .name("testStudy")
                .path("abas")
                .description("testing study")
                .shortDescription("short testing")
                .allowMemberNumber(0)
                .build();
        String organizer = "testing@co.kr";
        StudyDomain study = mockStudy(testStudy);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser principal = (LoginUser) authentication.getPrincipal();
        UserDomain user = principal.getUser();

        StudyDomain value = mockSetStudy(study, organizer);
        when(studyService.createStudy((StudyDomain) any(), (UserDomain) any())).thenReturn(value);


        mockMvc.perform(post("/admin/study/create")
                        .with(csrf())
                        .param("name", testStudy.getName())
                        .param("path", testStudy.getPath())
                        .param("description", testStudy.getDescription())
                        .param("shortDescription", testStudy.getShortDescription())
                        .param("allowMemberNumber", String.valueOf(testStudy.getAllowMemberNumber()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/study/list"))
                .andExpect(flash().attributeExists("msg"));
    }


    private StudyDomain mockStudy(CreateStudyForm form) {
        return StudyDomain.builder()
                .name(form.getName())
                .path(form.getPath())
                .description(form.getDescription())
                .shortDescription(form.getShortDescription())
                .allowMemberNumber(form.getAllowMemberNumber())
                .build();
    }

    private StudyDomain mockSetStudy(StudyDomain study, String organizer) {
        study.setOrganizer(organizer);
        study.setIdx(1L);
        log.info("mapping ::: {}", study);
        return study;
    }


}