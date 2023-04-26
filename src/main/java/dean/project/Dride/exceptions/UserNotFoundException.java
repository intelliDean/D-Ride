package dean.project.Dride.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;

import static dean.project.Dride.exceptions.ExceptionMessage.USER_NOT_FOUND;

public class UserNotFoundException extends DrideException {
    @Serial
    private static final long serialVersionUID = 1;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        this(USER_NOT_FOUND);
    }

    public UserNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
