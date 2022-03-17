package kr.co.won.config.datasources;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;

import javax.persistence.GenerationType;
import javax.sql.DataSource;
import java.sql.SQLException;


@Slf4j
@Profile(value = {"multi_database"})
@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class DataSourceConfiguration {

    @Bean
    @Primary
    @Qualifier(value = "primaryDataSourceProperties")
    @ConfigurationProperties(value = "spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @Qualifier(value = "dataSource")
    public DataSource dataSource(@Qualifier("primaryDataSourceProperties") DataSourceProperties dataSourceProperties) {
        log.info("basic datasource");
//        return DataSourceBuilder.create().build();
        HikariDataSource firstDataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        log.info("get first database maximum pool size ::: {}", firstDataSource.getMaximumPoolSize());
        return firstDataSource;
    }


    // get second properties
    @Bean
    @Qualifier(value = "ormDataSourceProperties")
    @ConfigurationProperties("spring.datasource.seconds")
    public DataSourceProperties ormDataSourceProperties() {
        log.info("get config properties");
        return new DataSourceProperties();
    }


    @Bean(name = "secondDatasource")
    @Qualifier(value = "secondDatasource")
//    @ConfigurationProperties("spring.datasource.seconds.configuration")
    public DataSource ormDataSource(@Qualifier("ormDataSourceProperties") DataSourceProperties dataSourceProperties) {
        log.info("create second Data source ");
        DataSourceBuilder.create().build();
        HikariDataSource secondDataSource = dataSourceProperties.initializeDataSourceBuilder().
                type(HikariDataSource.class).build();
        log.info("get second database maximum pool size ::: {}", secondDataSource.getMaximumPoolSize());
        return secondDataSource;
    }


}
