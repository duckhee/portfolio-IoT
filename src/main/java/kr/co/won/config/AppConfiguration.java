package kr.co.won.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class AppConfiguration {

    /**
     * null skip model mapping
     */
    @Primary
    @Bean(name = "skipModelMapper")
    @Qualifier(value = "skipModelMapper")
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                // this is null value not mapping
                .setSkipNullEnabled(true)
                // model mapper set object UnderScope
                .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE)
                .setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
        return modelMapper;
    }

    /**
     * null not skip model mapping
     */
    @Bean(name = "notSkipModelMapper")
    @Qualifier(value = "notSkipModelMapper")
    public ModelMapper notSkipModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                // this is null value not mapping
                .setSkipNullEnabled(false)
                // model mapper set object UnderScope
                .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE)
                .setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return passwordEncoder;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }// Register HttpSessionEventPublisher

    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }
/**
 @Bean FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter()
 {
 FilterRegistrationBean<ForwardedHeaderFilter> bean = new FilterRegistrationBean<>();
 bean.setFilter(new ForwardedHeaderFilter());
 return bean;
 }
 */
}
