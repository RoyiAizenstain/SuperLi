package employee.Exceptions;

public class InvalidInputException extends Exception{
    // Basic constructor
    public InvalidInputException() {
        super();
    }

    // Constructor with message
    public InvalidInputException(String message) {
        super(message);
    }

}
