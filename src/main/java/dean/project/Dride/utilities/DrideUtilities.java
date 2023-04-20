package dean.project.Dride.utilities;

import dean.project.Dride.data.dto.request.Location;
import dean.project.Dride.exceptions.DrideException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.security.SignatureException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;


public class DrideUtilities {
    public static final int NUMBER_OF_ITEMS_PER_PAGE = 5;
    public static final String USER_VERIFICATION_BASE_URL = "localhost:9090/api/v1/user/account/verify";
    public static final String DRIVER_WELCOME_MAIL_FILEPATH = "C:\\Users\\Dean\\IdeaProjects\\Dride\\Dride\\src\\main\\resources\\driver_welcome.txt";
    public static final String PASSENGER_WELCOME_MAIL_FILEPATH = "C:\\Users\\Dean\\IdeaProjects\\Dride\\Dride\\src\\main\\resources\\passenger_welcome.txt";
    public static final String ADMIN_INVITE_MAIL_FILEPATH = "C:\\Users\\Dean\\IdeaProjects\\new_uber\\uber_deluxe\\src\\main\\resources\\adminMail.txt";
    public final static String ADMIN_SUBJECT = "Admin Invitation";
    public final static String USER_SUBJECT = "Welcome to Dride";
    public static final String JSON_CONSTANT = "json";
    public static final String JWT_SECRET = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    public static final String TRANSPORT_MODE = "driving";
    public static final String UBER_DELUXE_TEST_IMAGE = "C:\\Users\\Dean\\IdeaProjects\\new_uber\\uber_deluxe\\src\\main\\resources\\repeatability-scalability-v5-1332458578.jpeg";

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
        return USER_VERIFICATION_BASE_URL + "?userId=" + userId + "&token=" + generateVerificationToken();
    }

    private static String generateVerificationToken() {
        Date expiration = Date.from(Instant.now().plusSeconds(86400));
        return Jwts.builder()
                .setIssuer("Dride")
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.decode(JWT_SECRET))
                .setExpiration(expiration)
                .setIssuedAt(Date.from(Instant.now()))
                .compact();
        //SignatureAlgorithm.HS512, jwtUtil.getJwtSecret()
    }

    public static boolean isTokenSigned(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(TextCodec.BASE64.decode(JWT_SECRET))
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
