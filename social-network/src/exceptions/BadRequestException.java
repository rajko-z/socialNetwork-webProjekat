package exceptions;

public class BadRequestException extends AppException{

    public BadRequestException(String message) {
        super(message);
    }
}
