package kr.co.won.config;

import kr.co.won.study.controller.AdminStudyController;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@WebMvcTest(controllers = {AdminStudyController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        SecurityConfig.class
                })
        })
// custom Web Slice Annotation
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(value = {MockitoExtension.class})
@Import(value = {TestWebMvcConfig.class, TestAppConfiguration.class})
public @interface WebSliceTestConfig {
}
