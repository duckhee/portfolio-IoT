package kr.co.won.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(value = "app")
public class AppProperties {

    private String host = "localhost";

    private String uploadFolderPath = "upload";

}
