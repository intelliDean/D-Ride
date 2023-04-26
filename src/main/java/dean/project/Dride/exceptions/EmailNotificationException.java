package dean.project.Dride.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;

import static dean.project.Dride.exceptions.ExceptionMessage.EMAIL_EXCEPTION;

public class EmailNotificationException extends DrideException {
    @Serial
    private static final long serialVersionUID = 1;
    public EmailNotificationException() {
        this(EMAIL_EXCEPTION);
    }

    public EmailNotificationException(String message) {
        super(message);
    }

    public EmailNotificationException(String message, HttpStatus status) {
        super(message, status);
    }
}
