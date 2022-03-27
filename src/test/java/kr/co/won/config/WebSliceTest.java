package kr.co.won.config;


import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Inherited
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(value = {MockitoExtension.class})
@Import(value = {TestWebMvcConfig.class, TestAppConfiguration.class})
public @interface WebSliceTest {
}
