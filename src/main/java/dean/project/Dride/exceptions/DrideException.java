package dean.project.Dride.exceptions;

import java.io.Serial;

public class DrideException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;
    public DrideException(String message) {
        super(message);
    }
}
