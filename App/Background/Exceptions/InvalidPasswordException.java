package App.Background.Exceptions;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException() {
        super();
    }

    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPasswordException(Throwable cause) {
        super(cause);
    }
}
