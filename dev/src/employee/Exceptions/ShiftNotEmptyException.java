package employee.Exceptions;

public class ShiftNotEmptyException extends Exception{
    public ShiftNotEmptyException() {
        super();
    }

    // Constructor with message
    public ShiftNotEmptyException(String message) {
        super(message);
    }
}
