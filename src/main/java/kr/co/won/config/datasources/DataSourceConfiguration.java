package kr.co.won.config.datasources;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/*
@Slf4j
@Configuration
@PropertySource(value = {"classpath:application.properties"})
*/

public class DataSourceConfiguration {
/*

    @Bean
    @Qualifier(value = "ormDataSourceProperties")
    @ConfigurationProperties("spring.datasource.seconds")
    public DataSourceProperties ormDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "ormDatasource")
    @Qualifier(value = "ormDatasource")
    @ConfigurationProperties("spring.datasource.seconds.configuration")
    public DataSource ormDataSource(@Qualifier("ormDataSourceProperties") DataSourceProperties dataSourceProperties) {
        log.info("create orm Data source ");
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }



    @Bean
    @Primary
    @Qualifier(value = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        log.info("basic datasource");
        return DataSourceBuilder.create().build();
    }
*/

}
