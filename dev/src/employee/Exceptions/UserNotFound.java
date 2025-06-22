package employee.Exceptions;

public class UserNotFound extends Exception{
    public UserNotFound() {
        super();
    }

    // Constructor with message
    public UserNotFound(String message) {
        super(message);
    }
}
