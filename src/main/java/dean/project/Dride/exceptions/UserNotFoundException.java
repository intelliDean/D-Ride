package dean.project.Dride.exceptions;

import java.io.Serial;

public class UserNotFoundException extends DrideException {
    @Serial
    private static final long serialVersionUID = 1;

    public UserNotFoundException(String message) {
        super(message);
    }
}
