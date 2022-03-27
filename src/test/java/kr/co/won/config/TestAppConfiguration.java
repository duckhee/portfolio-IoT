package kr.co.won.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.config.security.jwt.JwtAuthEntryPoint;
import kr.co.won.config.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
@RequiredArgsConstructor
public class TestAppConfiguration {

    /**
     * null skip model mapping
     */
    @Primary
    @Bean(name = "skipModelMapper")
    @Qualifier(value = "skipModelMapper")
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                // model mapper set object UnderScope
                .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE)
                .setSourceNameTokenizer(NameTokenizers.UNDERSCORE)
                // model mapper not update null value
                .setSkipNullEnabled(true);
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
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return String.valueOf(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return String.valueOf(rawPassword).equals(encodedPassword);
            }
        };
        return passwordEncoder;
    }

    /**
     * JWT Token util
     */
    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil();
    }

    @Bean
    public JwtAuthEntryPoint jwtAuthEntryPoint(){
        return new JwtAuthEntryPoint();
    }

}
