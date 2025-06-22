package employee.Exceptions;

public class RoleNotFoundException extends Exception{
    // Basic constructor
    public RoleNotFoundException() {
        super();
    }

    // Constructor with message
    public RoleNotFoundException(String message) {
        super(message);
    }
}
