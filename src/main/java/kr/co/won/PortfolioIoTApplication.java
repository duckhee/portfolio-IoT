package kr.co.won;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.integration.annotation.IntegrationComponentScan;

@EnableJpaAuditing // jpa auditing setting
@EnableCaching // caching setting
@ConfigurationPropertiesScan
//@IntegrationComponentScan
@SpringBootApplication
public class PortfolioIoTApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioIoTApplication.class, args);
    }

}
