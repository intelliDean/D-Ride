package dean.project.Dride.utilities;

import dean.project.Dride.data.dto.request.CreateUser;
import dean.project.Dride.data.dto.request.Location;
import dean.project.Dride.data.models.User;
import dean.project.Dride.exceptions.DrideException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;

import static dean.project.Dride.utilities.Constants.*;

@AllArgsConstructor
public class DrideUtilities {
    @Value("${jwt.secret.key}")
    private static String jwtSecret;
    private static PasswordEncoder passwordEncoder;
    public static User createUser(CreateUser createUser) {
        return User.builder()
                .name(createUser.getName())
                .email(createUser.getEmail())
                .password(passwordEncoder.encode(createUser.getPassword()))
                .createdAt(LocalDateTime.now().toString())
                .roles(new HashSet<>())
                .build();
    }


    public static String driverWelcomeMail() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DRIVER_WELCOME_MAIL_FILEPATH))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException exception) {
            throw new DrideException(exception.getMessage());
        }
    }

    public static String passengerWelcomeMail() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PASSENGER_WELCOME_MAIL_FILEPATH))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException exception) {
            throw new DrideException(exception.getMessage());
        }
    }

    public static String getAdminMailTemplate() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_INVITE_MAIL_FILEPATH))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException exception) {
            throw new DrideException(exception.getMessage());
        }
    }

    public static String generateVerificationLink(Long userId) {
        return "localhost:9090/api/v1/user/account/verify" + "?userId=" + userId + "&token=" + generateVerificationToken();
    }

    private static String generateVerificationToken() {
        Date expiration = Date.from(Instant.now().plusSeconds(86400));
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.decode(jwtSecret))
                .setExpiration(expiration)
                .setIssuedAt(Date.from(Instant.now()))
                .compact();
    }

    public static int calculateAge(String dateOfBirth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthDate = LocalDate.parse(dateOfBirth, formatter);
        LocalDate currentDate = LocalDate.now();

        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }

    public static boolean isTokenSigned(String token) {
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


    public static String buildLocation(Location location) {
        return location.getHouseNumber() + "," + location.getStreet() + "," + location.getCity() + location.getState();
    }

    public static BigDecimal calculateRideFare(String distance) {
        return BigDecimal
                .valueOf(Double.parseDouble(distance.split("k")[0]))
                .multiply(BigDecimal.valueOf(1000));
    }
}
