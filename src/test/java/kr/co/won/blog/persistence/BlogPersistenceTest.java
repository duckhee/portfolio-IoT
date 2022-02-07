package kr.co.won.blog.persistence;

import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.blog.factory.BlogFactory;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.factory.UserFactory;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@Transactional
@Rollback
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(value = {UserFactory.class, BlogFactory.class})
class BlogPersistenceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BlogPersistence blogPersistence;

    @Autowired
    private BlogReplyPersistence blogReplyPersistence;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private BlogFactory blogFactory;

    @AfterEach
    void dataInit() {
        blogPersistence.deleteAll();
    }

    @DisplayName(value = "01. create blog jpa Tests")
    @Test
    void createBlogTests() throws MalformedURLException {
        String projectUrl = new URL("http://github.com/project/test").toString();
        String content = "content";
        String title = "title";
        String writerEmail = "tester@co.kr";
        String tester = "tester";
        BlogDomain newBlogs = BlogDomain.builder()
                .title(title)
                .content(content)
                .projectUrl(projectUrl)
                .writerEmail(writerEmail)
                .writer(tester)
                .build();
        BlogDomain savedBlog = blogPersistence.save(newBlogs);
        assertThat(newBlogs.getIdx()).isNotNull();
        assertThat(savedBlog).isEqualTo(newBlogs);
    }

    @DisplayName(value = "02. find blog jpa Tests")
    @Test
    void readBlogTests() {

    }

    @DisplayName(value = "03. update blog jpa Tests")
    @Test
    void updateBlogTests() {

    }

    @DisplayName(value = "04. delete blog jpa Tests")
    @Test
    void deleteBlogTests() {

    }

    @DisplayName(value = "04. delete blog jpa Tests")
    @Test
    void bulkDeleteBlogTests() {
        UserDomain testUser = userFactory.testUser("tester", "tester@co.kr", "1234");
        List<BlogDomain> blogs = blogFactory.makeBulkBlogWithReply(10, "title", "content", testUser, 10);
        List<Long> blogIdxes = blogs.stream().map(BlogDomain::getIdx).collect(Collectors.toList());
        entityManager.clear();

        blogPersistence.deleteAllByIdx(blogIdxes);
        List<BlogDomain> allById = blogPersistence.findAllById(blogIdxes);
        assertThat(allById).isNullOrEmpty();
    }

    @DisplayName(value = "05. find blog one using writer Tests")
    @Test
    void findBlogWithWriterLimitOne() {
        UserDomain testUser = userFactory.testUser("tester", "tester@co.kr", "1234");
        List<BlogDomain> blogs = blogFactory.makeBulkBlogWithReply(10, "title", "content", testUser, 10);
        entityManager.clear();
        Optional<BlogDomain> findBlogLimitOne = blogPersistence.findFirstByWriterOrderByCreatedAtDesc("tester");
        log.info("find blog limit one :::: {}", findBlogLimitOne.get());
    }

    @DisplayName(value = "05. find blog 10 using writer Tests")
    @Test
    void findBlogWithWriterLimitTen() {
        UserDomain testUser = userFactory.testUser("tester", "tester@co.kr", "1234");
        List<BlogDomain> blogs = blogFactory.makeBulkBlogWithReply(20, "title", "content", testUser, 10);
        entityManager.clear();
        List<BlogDomain> findBlogLimitTen = blogPersistence.findTop10ByWriterOrderByCreatedAtDesc("tester");
        assertThat(findBlogLimitTen.size()).isEqualTo(10);

    }
}