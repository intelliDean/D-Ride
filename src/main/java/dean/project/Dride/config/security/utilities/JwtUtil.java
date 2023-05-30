package dean.project.Dride.config.security.utilities;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static dean.project.Dride.utilities.Constants.APP_NAME;

@Component
public class JwtUtil {
    private final Key key;
    @Value("${access}")
    public Long accessExpiration;
    @Value("${refresh}")
    public Long refreshExpiration;

    @Autowired
    public JwtUtil(Key key) {
        this.key = key;
    }


    public String extractUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateRefreshToken(String email) {
        Date expiredAt = Date.from(Instant.now().plusSeconds(refreshExpiration));
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setIssuedAt(Date.from(Instant.now()))
                .setSubject(email)
                .setExpiration(expiredAt)
                .signWith(key)
                .compact();
    }

    public String generateAccessToken(Map<String, Object> claims, String email) {
        Date expiredAt = Date.from(Instant.now().plusSeconds(accessExpiration));
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setIssuedAt(Date.from(Instant.now()))
                .setClaims(claims)
                .setSubject(email)
                .setExpiration(expiredAt)
                .signWith(key)
                .compact();
    }

    public String generateVerificationLink(Long userId) {
        return "localhost:9090/api/v1/user/account/verify" + "?userId=" + userId + "&token=" + generateVerificationToken();
    }

    private String generateVerificationToken() {
        Date expiration = Date.from(Instant.now().plusSeconds(86400));
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .signWith(key)
                .setExpiration(expiration)
                .setIssuedAt(Date.from(Instant.now()))
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true; // Token is signed
        } catch (SignatureException e) {
            return false; //unsigned token
        } catch (JwtException e) {
            return false; // invalid token
        }
    }
}
