package dean.project.Dride.utilities;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

public class DrideUtilities {
    public static final int NUMBER_OF_ITEMS_PER_PAGE = 3;
    private static final String USER_VERIFICATION_BASE_URL ="localhost:8080/api/v1/user/account/verify";

    public static String getMailTemplate() {
        try (BufferedReader reader = new BufferedReader(new FileReader(
                "C:\\Users\\Dean\\IdeaProjects\\Dride\\Dride\\src\\main\\resources\\welcome.txt"))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static String getAdminMailTemplate() {
        try (BufferedReader reader = new BufferedReader(new FileReader(
                "C:\\Users\\Dean\\IdeaProjects\\Dride\\Dride\\src\\main\\resources\\adminMail.txt"))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static String generateVerificationLink(Long userId) {
        return USER_VERIFICATION_BASE_URL+"?userId="+userId+"&token="+generateVerificationToken();
    }

    public static String generateVerificationToken() {
        return Jwts.builder()
                .setIssuer("dride")
                .signWith(SignatureAlgorithm.HS256,
                        TextCodec.BASE64.decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E="))
                .setIssuedAt(new Date())
                .compact();
    }
    public static boolean isValidToken(String token) {
        return Jwts.parser().isSigned(token);
    }

}
