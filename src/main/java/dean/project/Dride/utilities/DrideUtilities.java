package dean.project.Dride.utilities;

import dean.project.Dride.config.security.util.JwtUtil;
import dean.project.Dride.data.dto.request.Location;
import dean.project.Dride.exceptions.DrideException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DrideUtilities {
    public static final int NUMBER_OF_ITEMS_PER_PAGE = 3;
    private static final String USER_VERIFICATION_BASE_URL = "localhost:9090/api/v1/user/account/verify";
    public static final String WELCOME_MAIL_TEMPLATE_LOCATION = "C:\\Users\\Dean\\IdeaProjects\\Dride\\Dride\\src\\main\\resources\\welcome.txt";
    public static final String EMAIL_REGEX_STRING = "^[a-zA-Z0-9_+&*-]+(?:\\\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,7}$";
    public static final String ADMIN_INVITE_MAIL_TEMPLATE_LOCATION = "C:\\Users\\semicolon\\Documents\\code\\springboot-projects\\uber_deluxe\\src\\main\\resources\\adminMail.txt";

    public static final String JSON_CONSTANT = "json";
    public static final String DB_PASSWORD="@Tiptop2059!";

    public static final String TRANSPORT_MODE = "driving";
    public static final String UBER_DELUXE_TEST_IMAGE = "C:\\Users\\Dean\\IdeaProjects\\Dride\\Dride\\src\\main\\resources\\repeatability-scalability-v5-1332458578.jpeg";

    private final JwtUtil jwtUtil;

    public static String getMailTemplate() {
        try (BufferedReader reader = new BufferedReader(new FileReader(
                WELCOME_MAIL_TEMPLATE_LOCATION))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException exception) {
            throw new DrideException(exception.getMessage());
        }
    }

    public static String getAdminMailTemplate() {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(ADMIN_INVITE_MAIL_TEMPLATE_LOCATION))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException exception) {
            throw new DrideException(exception.getMessage());
        }
    }

    public static String generateVerificationLink(Long userId) {
        return USER_VERIFICATION_BASE_URL + "?userId=" + userId + "&token=" + generateVerificationToken();
    }

    private static String generateVerificationToken() {
        return Jwts.builder()
                .setIssuer("uber_deluxe")
                .signWith(SignatureAlgorithm.ES512,
                       "secret")
                .setIssuedAt(new Date())
                .compact();
    }
//TextCodec.BASE64.decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")
    public static boolean isValidToken(String token) {
        return Jwts.parser()
                .isSigned(token);
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
