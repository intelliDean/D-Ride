package dean.project.Dride.utilities;


import dean.project.Dride.data.dto.request.Location;

import java.math.BigDecimal;

public class DrideUtilities {
    public static String buildLocation(Location location) {
        return location.getHouseNumber() + "," + location.getStreet() + "," + location.getCity() + location.getState();
    }

    public static BigDecimal calculateRideFare(String distance) {
        return BigDecimal
                .valueOf(Double.parseDouble(distance.split("k")[0]))
                .multiply(BigDecimal.valueOf(1000));
    }
//     public static String driverWelcomeMail() {
//        try (BufferedReader reader = new BufferedReader(new FileReader(DRIVER_WELCOME_MAIL_FILEPATH))) {
//            return reader.lines().collect(Collectors.joining());
//        } catch (IOException exception) {
//            throw new DrideException(exception.getMessage());
//        }
//    }
}
