package kr.co.won.study.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import kr.co.won.auth.TestUser;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.factory.StudyFactory;
import kr.co.won.study.form.CreateStudyForm;
import kr.co.won.study.persistence.StudyPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.type.UserRoleType;
import kr.co.won.user.factory.UserFactory;
import kr.co.won.user.persistence.UserPersistence;
import org.junit.Assume;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(value = {UserFactory.class, StudyFactory.class})
class AdminStudyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserPersistence userPersistence;

    @Autowired
    private StudyPersistence studyPersistence;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private StudyFactory studyFactory;

    @AfterEach
    public void dataInit() {
        userPersistence.deleteAll();
    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "01. create study page Test - with GET(success) ROLE(ADMIN)")
    @Test
    void createStudyPageTests_withADMIN() throws Exception {
        mockMvc.perform(get("/admin/study/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("createStudyForm"))
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("admin/study/studyCreatePage"));
    }

    @TestUser(email = "tester@co.kr", authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "01. create study Page Test - with POST(success) ROLE(ADMIN)")
    @Test
    void createStudyDoTests_withADMIN() throws Exception {
        UserDomain findUser = userPersistence.findByEmail("tester@co.kr").orElse(null);
        // assume test
//        assumeNotNull(findUser);
        CreateStudyForm studyForm = CreateStudyForm.builder()
                .allowMemberNumber(0)
                .name("study")
                .path("/study/test")
                .shortDescription("testing short description")
                .description("description")
                .build();
        mockMvc.perform(post("/admin/study/create")
                        .with(csrf())
                        .param("name", studyForm.getName())
                        .param("path", studyForm.getPath())
                        .param("shortDescription", studyForm.getShortDescription())
                        .param("allowMemberNumber", String.valueOf(studyForm.getAllowMemberNumber()))
                        .param("description", studyForm.getDescription()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/study/list"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(view().name("redirect:/admin/study/list"));
    }

    @TestUser(email = "tester@co.kr", authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "02. list study Page Test - with GET(success) ROLE(ADMIN)")
    @Test
    void listStudyPageTests_withADMIN() throws Exception {
        UserDomain findUser = userPersistence.findByEmail("tester@co.kr").orElse(null);
        Assume.assumeNotNull(findUser);
        StudyDomain testStudy = studyFactory.makeStudy("/testing", "testStudy", 0, findUser);
        mockMvc.perform(get("/admin/study/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("page"))
                .andExpect(view().name("admin/study/studyListPage"));
    }

    @TestUser(authLevel = UserRoleType.ADMIN, email = "tester@co.kr")
    @DisplayName(value = "03. find study Page Test - with GET(success) ROLE(ADMIN)")
    @Test
    void findStudyPageTests_withADMIN() throws Exception {
        UserDomain findUser = userPersistence.findByEmail("tester@co.kr").orElse(null);
        Assume.assumeNotNull(findUser);
        StudyDomain findStudy = studyFactory.makeStudy("testing", "testStudy", 0, findUser);
        mockMvc.perform(get("/admin/study/"+findStudy.getPath()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("study", "user"))
                .andExpect(view().name("admin/study/studyInformationPage"));
    }

    @TestUser(authLevel = UserRoleType.ADMIN, email = "tester@co.kr")
    @DisplayName(value = "04. update study Page Test - with GET(success) ROLE(ADMIN)")
    @Test
    void updateStudyPageTest_withADMIN() throws Exception {
        UserDomain findUser = userPersistence.findByEmail("tester@co.kr").orElse(null);
        Assume.assumeNotNull(findUser);
        StudyDomain findStudy = studyFactory.makeStudy("testing", "testStudy", 0, findUser);
        mockMvc.perform(get("/admin/study/update/" + findStudy.getPath()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("updateStudyForm"))
                .andExpect(view().name("admin/study/studyUpdatePage"));
    }
}