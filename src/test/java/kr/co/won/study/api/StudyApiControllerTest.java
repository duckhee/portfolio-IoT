package kr.co.won.study.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.TestUser;
import kr.co.won.blog.factory.BlogFactory;
import kr.co.won.config.RestDocsConfiguration;
import kr.co.won.study.form.CreateStudyForm;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.factory.UserFactory;
import kr.co.won.user.persistence.UserPersistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(value = {RestDocsConfiguration.class, UserFactory.class, BlogFactory.class})
class StudyApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private BlogFactory blogFactory;
    @Autowired
    private UserPersistence userPersistence;

    @AfterEach
    void dataInit() {
        userPersistence.deleteAll();
    }

    @Disabled
    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "01. study Create Test - with ADMIN")
    @Test
    void studyCreateTests_withADMIN() throws Exception {

        mockMvc.perform(post("/api/studies"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "01. spring parameter test")
    @Test
    void springTests() throws Exception {
        List<Long> list = List.of(1L, 2L, 3L);
        Map<String, List> mapping = new HashMap<>();
        mapping.put("study", list);
        mockMvc.perform(delete("/api/studies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapping)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}