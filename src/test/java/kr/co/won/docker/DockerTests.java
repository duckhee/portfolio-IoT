package kr.co.won.docker;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Slf4j
@Disabled
@ActiveProfiles(value = {"docker"})
@DataJpaTest
// set testing database
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers(disabledWithoutDocker = true)
public class DockerTests {


    @Container
    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0.27")
            .withDatabaseName("test")
            .withUsername("root")
            .withPassword("test");

    @Autowired
    private DataSource dataSource;

    @Test
    void containerTest() {
        String jdbcUrl = mySQLContainer.getJdbcUrl();
        log.info("docker container information : {}", mySQLContainer.getContainerInfo());
        log.info("jbc url : {}", jdbcUrl);
        log.info("user name : {}", mySQLContainer.getUsername());
        log.info("user password : {}", mySQLContainer.getPassword());
        log.info("get database name : {}", mySQLContainer.getDatabaseName());
        log.info("dta source = {}", dataSource.getClass().getName());
    }
}
