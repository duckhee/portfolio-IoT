package kr.co.won.study.persistence;


import kr.co.won.auth.AuthUser;
import kr.co.won.user.persistence.UserPersistence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = Replace.NONE)
class StudyPersistenceTest {

    @Autowired
    private UserPersistence userPersistence;

    @Autowired
    private StudyPersistence studyPersistence;

    @DisplayName(value = "01. create study Persistence Tests")
    @Test
    void createStudyPersistenceTests() {

    }

}