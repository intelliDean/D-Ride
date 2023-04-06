package dean.project.Dride.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionMessage {
    USER_NOT_FOUND("User with ID %s could not be found"),
    PASSENGER_WITH_NAME("Passenger with name: %s could not be found"),
    PASSENGER_WITH_ID("Passenger with id: %s could not be found");
    private final String message;

}
