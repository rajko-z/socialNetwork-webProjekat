package exceptions;

public class AppException extends RuntimeException{
    private String message;

    public AppException() {
        super();
    }

    public AppException(String message) {
        super(message);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
