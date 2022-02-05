package kr.co.won.blog.persistence;

import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.blog.factory.BlogFactory;
import kr.co.won.user.factory.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Rollback
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(value = {UserFactory.class, BlogFactory.class})
class BlogPersistenceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BlogPersistence blogPersistence;

    @Autowired
    private BlogReplyPersistence blogReplyPersistence;

}