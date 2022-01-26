package kr.co.won.user.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ExtendWith(value = {MockitoExtension.class})
@AutoConfigureMockMvc
class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @DisplayName(value = "01. user create api test - success")
    @Test
    void userCreateSuccessTests() throws Exception {

    }

}