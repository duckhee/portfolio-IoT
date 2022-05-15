package kr.co.won.main;

import kr.co.won.auth.TestUser;
import kr.co.won.config.RestDocsConfiguration;
import kr.co.won.user.domain.type.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.relaxedLinks;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(value = {
        RestDocsConfiguration.class
})
class MainApiControllerTest {

    private final String HAL_JSON = "HAL JSON";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserPersistence userPersistence;

    @BeforeEach
    void setup() {
        userPersistence.deleteAll();
    }


    @DisplayName(value = "01. index link resources Tests - not login user")
    @Test
    void indexLinkResourceTests_withNotLogin() throws Exception {
        mockMvc.perform(get("/api"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @TestUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "01. index link resource Tests - with ADMIN")
    @Test
    void indexLinkResourceTests_withADMIN() throws Exception {
        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("index-link-resource",
                        /** request Headers */
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(HAL_JSON).optional()
                        ),
                        /** Index Links */
                        relaxedLinks(
                                linkWithRel("self").description("목차에 해당되는 기능에 대한 설명된 링크에 대한 정보").optional(),
                                linkWithRel("users").description("사용자에 관련된 기능에 대한 링크 정보").optional(),
                                linkWithRel("blogs").description("블로그에 관련된 기능에 대한 링크 정보").optional(),
                                linkWithRel("studies").description("스터디에 관련된 기능에 대한 링크 정보").optional(),
                                linkWithRel("iot").description("IoT에 관련된 기능에 대한 링크 정보").optional(),
                                linkWithRel("login").description("로그인 경로를 담고 있는 링크").optional()
                        )
                ));
    }


}