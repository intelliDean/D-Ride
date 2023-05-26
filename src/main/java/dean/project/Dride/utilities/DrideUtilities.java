package dean.project.Dride.utilities;

import dean.project.Dride.data.dto.request.Location;
import dean.project.Dride.exceptions.DrideException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import static dean.project.Dride.utilities.Constants.*;

public class DrideUtilities {
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
    public static String buildLocation(Location location) {
        return location.getHouseNumber() + "," + location.getStreet() + "," + location.getCity() + location.getState();
    }

    public static BigDecimal calculateRideFare(String distance) {
        return BigDecimal
                .valueOf(Double.parseDouble(distance.split("k")[0]))
                .multiply(BigDecimal.valueOf(1000));
    }
}
