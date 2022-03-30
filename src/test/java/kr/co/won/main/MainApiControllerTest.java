package kr.co.won.main;

import kr.co.won.auth.AuthUser;
import kr.co.won.config.RestDocsConfiguration;
import kr.co.won.config.TestAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(value = {
        TestAppConfiguration.class,
        RestDocsConfiguration.class
})
class MainApiControllerTest {

    @Autowired
    private MockMvc mockMvc;


}