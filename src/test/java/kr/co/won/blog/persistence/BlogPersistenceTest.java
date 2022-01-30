package kr.co.won.blog.persistence;

import kr.co.won.blog.domain.BlogReplyDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Rollback
@AutoConfigureTestDatabase(replace = Replace.NONE)
class BlogPersistenceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BlogPersistence blogPersistence;

    @Autowired
    private BlogReplyPersistence blogReplyPersistence;

}