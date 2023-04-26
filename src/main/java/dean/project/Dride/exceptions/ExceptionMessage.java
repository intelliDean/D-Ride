package dean.project.Dride.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionMessage {
    public final static String USER_NOT_FOUND="User could not be found";
    public final static String PASSENGER_WITH_NAME="Passenger with name: %s could not be found";
    public final static String PASSENGER_WITH_ID="Passenger with id: %s could not be found";
    public final static String EMAIL_EXCEPTION="Email could not be sent";
    public final static String DRIVER_REG_FAILED="Driver Registration failed";
    public final static String IMAGE_EXCEPTION="Image could not be sent";
    public final static String TOO_YOUNG="You are too young to be a driver on our platform";
    public final static String UPDATE_FAILED="Update failed";
    public final static String PASSENGER_REG_FAILED="Registration Failed! Please check your connection and try again later";
}
