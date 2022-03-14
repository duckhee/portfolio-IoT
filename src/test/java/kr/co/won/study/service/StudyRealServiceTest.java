package kr.co.won.study.service;

import kr.co.won.auth.AuthUser;
import kr.co.won.study.factory.StudyFactory;
import kr.co.won.user.factory.UserFactory;
import org.intellij.lang.annotations.JdkConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(value = {UserFactory.class, StudyFactory.class})
class StudyRealServiceTest {

    @Autowired
    private StudyService studyService;
}