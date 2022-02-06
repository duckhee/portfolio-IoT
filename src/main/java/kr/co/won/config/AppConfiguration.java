package kr.co.won.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                // this is null value not mapping
                .setSkipNullEnabled(true)
                // modelmapper set object UnderScope
                .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE)
                .setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return passwordEncoder;
    }
}
