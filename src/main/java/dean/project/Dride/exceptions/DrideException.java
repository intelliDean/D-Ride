package dean.project.Dride.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class DrideException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    @Getter
    @Setter
    private HttpStatus status;

    public DrideException() {
        this("An error occurred!");
    }

    public DrideException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public DrideException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }
}
