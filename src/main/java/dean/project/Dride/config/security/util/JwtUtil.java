package dean.project.Dride.config.security.util;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static dean.project.Dride.utilities.Constants.APP_NAME;

@AllArgsConstructor
@Getter
public class JwtUtil {

    private final String jwtSecret;

    public boolean isTokenSigned(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .isSigned(token);
    }

    public String extractUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateRefreshToken(String email) {
        Date refreshExpiration = Date.from(
                Instant.now()
                        .plusSeconds(3600 * 24));
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(email)
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS512,
                        TextCodec.BASE64.decode(jwtSecret))
                .compact();
    }

    public String generateAccessToken(Map<String, Object> claims, String email) {
        Date accessExpiration = Date.from(Instant.now().plusSeconds(3600));
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setIssuedAt(new Date())
                .setClaims(claims)
                .setSubject(email)
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS512,
                        TextCodec.BASE64.decode(jwtSecret))
                .compact();
    }
    public  String generateVerificationLink(Long userId) {
        return "localhost:9090/api/v1/user/account/verify" + "?userId=" + userId + "&token=" + generateVerificationToken();
    }

    private  String generateVerificationToken() {
        Date expiration = Date.from(Instant.now().plusSeconds(86400));
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.decode(jwtSecret))
                .setExpiration(expiration)
                .setIssuedAt(Date.from(Instant.now()))
                .compact();
    }

    public boolean tokenSigned(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(TextCodec.BASE64.decode(jwtSecret))
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
