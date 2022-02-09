package kr.co.won.config;

import kr.co.won.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc custom add Interceptor or Convertor
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AppProperties appProperties;

    // add resource config
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // custom upload path
        registry.addResourceHandler("/uploads")
                .addResourceLocations(appProperties.getUploadFolderPath());

    }
}
