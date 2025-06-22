package employee.Exceptions;

public class RoleExistsException extends Exception{
    // Basic constructor
    public RoleExistsException() {
        super();
    }

    // Constructor with message
    public RoleExistsException(String message) {
            super(message);
        }
}
