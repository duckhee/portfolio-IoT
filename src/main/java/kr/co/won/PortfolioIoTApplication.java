package kr.co.won;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

//@IntegrationComponentScan
@SpringBootApplication
public class PortfolioIoTApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioIoTApplication.class, args);
    }

}
