package kr.co.won.blog.persistence;

import groovy.transform.Immutable;
import kr.co.won.blog.domain.BlogResourceDomain;
import kr.co.won.blog.factory.BlogFactory;
import kr.co.won.user.factory.UserFactory;
import kr.co.won.user.persistence.UserPersistence;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@Slf4j
@DataJpaTest
@Transactional
@Rollback(value = true)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(value = {UserFactory.class, BlogFactory.class})
class BlogResourcePersistenceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserPersistence userPersistence;

    @Autowired
    private BlogPersistence blogPersistence;

    @Autowired
    private BlogResourcePersistence blogResourcePersistence;

    @Autowired
    private BlogFactory blogFactory;

    @Autowired
    private UserFactory userFactory;

    @AfterEach
    public void dataInit() {
        blogPersistence.deleteAll();
        blogPersistence.deleteAll();
        userPersistence.deleteAll();
    }

    @DisplayName(value = "01. blog resource create Tests")
    @Test
    void createBlogResourceTests() {
        BlogResourceDomain blogResourceBuilder = BlogResourceDomain.builder()
                .extension("ext")
                .originalName("testing")
                .saveFileName(UUID.randomUUID().toString())
                .fileSize("200")
                .build();
        blogResourcePersistence.save(blogResourceBuilder);
    }

    @DisplayName(value = "04. blog resource in query test")
    @Test
    void resourceInQueryTests() {
        List<BlogResourceDomain> list = new ArrayList<>();
        String saveFileName1 = UUID.randomUUID().toString();
        BlogResourceDomain blogResourceBuilder = BlogResourceDomain.builder()
                .extension("ext")
                .originalName("testing")
                .saveFileName(saveFileName1)
                .fileSize("200")
                .build();
        list.add(blogResourceBuilder);
        String saveFileName2 = UUID.randomUUID().toString();
        BlogResourceDomain blogResourceBuilder2 = BlogResourceDomain.builder()
                .extension("ext")
                .originalName("testing")
                .saveFileName(saveFileName2)
                .fileSize("200")
                .build();
        list.add(blogResourceBuilder2);
        String saveFileName3 = UUID.randomUUID().toString();
        BlogResourceDomain blogResourceBuilder3 = BlogResourceDomain.builder()
                .extension("ext")
                .originalName("testing")
                .saveFileName(saveFileName3)
                .fileSize("200")
                .build();
        list.add(blogResourceBuilder3);

        blogResourcePersistence.saveAll(list);
        // caching value delete
        entityManager.clear();
        List<String> nameList = List.of(saveFileName1,  saveFileName3);
        List<BlogResourceDomain> bySaveFileNameIn = blogResourcePersistence.findBySaveFileNameIn(nameList);
        assertThat(bySaveFileNameIn.size()).isEqualTo(nameList.size());

    }
}