package kr.co.won.config.security.jwt;

import com.auth0.jwt.JWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.co.won.auth.LoginUser;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * JWT Token library
 */
@Component
public class JwtTokenUtil {

    private static final String secretKey = "secret";

    // setting token live time 5 min
    public static final long JWT_TOKEN_VALIDITY = 5 * 60;

    // get username get
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    // get user password check
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // make access token
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    // check token expired time
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // get token expired time
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // generate token
    public String generateToken(String id) {
        return generateToken(id, new HashMap<>());
    }

    // generate token setting
    public String generateToken(String id, Map<String, Object> claims) {
        return doGenerateToken(id, claims);
    }

    // token build
    private String doGenerateToken(String id, Map<String, Object> claims) {

        return Jwts.builder()
                .setClaims(claims)
                .setId(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    // verified token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

}
