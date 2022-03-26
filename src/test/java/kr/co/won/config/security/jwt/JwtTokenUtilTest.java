package kr.co.won.config.security.jwt;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtTokenUtilTest {

    // secret key가 짧으면 에러가 난다
    private final String secretKey = "secretKey-test-authorization-jwt-manage-token";


    @DisplayName("토큰 정상 발급")
    @Test
    void successTest() {
        JwtTokenUtil jwtManager = new JwtTokenUtil();
        String username = "testuser-1";
        String accessToken = jwtManager.generateToken(username);
        assertThat(username).isEqualTo(jwtManager.getUsernameFromToken(accessToken));
    }


}

