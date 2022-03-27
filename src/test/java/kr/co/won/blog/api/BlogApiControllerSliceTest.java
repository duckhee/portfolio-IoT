package kr.co.won.blog.api;

import kr.co.won.auth.TestMockUser;
import kr.co.won.blog.api.assembler.BlogAssembler;
import kr.co.won.blog.service.BlogService;
import kr.co.won.config.WebSliceTest;
import kr.co.won.config.security.AdminSecurityConfiguration;
import kr.co.won.config.security.SecurityConfiguration;
import kr.co.won.user.domain.UserRoleType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@WebMvcTest(controllers = {
        BlogApiController.class
}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                SecurityConfiguration.class,
                AdminSecurityConfiguration.class
        })
})
@WebSliceTest
class BlogApiControllerSliceTest {

    @MockBean
    private BlogService blogService;

    @MockBean
    private BlogAssembler blogAssembler;

    @MockBean
    private PagedResourcesAssembler pagedResourcesAssembler;

    @Autowired
    private MockMvc mockMvc;


    @TestMockUser(authLevel = UserRoleType.ADMIN)
    @DisplayName(value = "01. Blog Api Create Slice Tests - with ADMIN")
    @Test
    void createSliceBlogApiTests_withADMIN() throws Exception {

        mockMvc.perform(post("/api/blogs"))
                .andExpect(status().isCreated());

    }
}