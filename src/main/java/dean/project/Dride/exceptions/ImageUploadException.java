package dean.project.Dride.exceptions;

import java.io.Serial;

public class ImageUploadException extends DrideException{
    @Serial
    private static final long serialVersionUID = 1;
    public ImageUploadException(String message) {
        super(message);
    }
}
