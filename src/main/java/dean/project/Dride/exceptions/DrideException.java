package dean.project.Dride.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

import static dean.project.Dride.utilities.Constants.ERROR_OCCUR;

public class DrideException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    @Getter
    @Setter
    private HttpStatus status;

    public DrideException() {
        this(ERROR_OCCUR);
    }

    public DrideException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public DrideException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }
}
