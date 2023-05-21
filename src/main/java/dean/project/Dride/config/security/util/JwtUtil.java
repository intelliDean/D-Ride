package dean.project.Dride.config.security.util;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static dean.project.Dride.utilities.Constants.ISSUER;

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
                .setIssuer(ISSUER)
                .setSubject(email)
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS512,
                        TextCodec.BASE64.decode(jwtSecret))
                .compact();
    }

    public String generateAccessToken(Map<String, Object> claims, String email) {
        Date accessExpiration = Date.from(Instant.now().plusSeconds(3600));
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setClaims(claims)
                .setSubject(email)
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS512,
                        TextCodec.BASE64.decode(jwtSecret))
                .compact();
    }

}
