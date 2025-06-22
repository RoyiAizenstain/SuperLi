package employee.Exceptions;

public class EmployeeNotFoundException extends Exception{
    // Basic constructor
    public EmployeeNotFoundException() {
        super();
    }

    // Constructor with message
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
