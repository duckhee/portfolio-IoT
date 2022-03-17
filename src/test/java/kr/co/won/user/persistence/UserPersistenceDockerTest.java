package kr.co.won.user.persistence;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(value = {"docker"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.AUTO_CONFIGURED)
@Testcontainers(disabledWithoutDocker = true)
class UserPersistenceDockerTest {

    @Container
    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0.27")
            .withDatabaseName("test")
            .withUsername("root")
            .withPassword("test");

    @Autowired
    private UserPersistence userPersistence;




}