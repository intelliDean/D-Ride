package dean.project.Dride.exceptions;

import java.io.Serial;

public class EmailNotificationException extends DrideException {
    @Serial
    private static final long serialVersionUID = 1;

    public EmailNotificationException(String message) {
        super(message);
    }
}
