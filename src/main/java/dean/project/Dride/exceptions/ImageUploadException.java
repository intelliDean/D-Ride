package dean.project.Dride.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;

import static dean.project.Dride.exceptions.ExceptionMessage.IMAGE_EXCEPTION;

public class ImageUploadException extends DrideException{
    @Serial
    private static final long serialVersionUID = 1;
    public ImageUploadException(String message) {
        super(message);
    }
    public ImageUploadException() {
        this(IMAGE_EXCEPTION);
    }
    public ImageUploadException(String message, HttpStatus status) {
        super(message, status);
    }
}
