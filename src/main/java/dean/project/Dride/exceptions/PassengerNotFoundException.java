package dean.project.Dride.exceptions;

import java.io.Serial;

public class PassengerNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    public PassengerNotFoundException(String message) {
        super(message);
    }
}
