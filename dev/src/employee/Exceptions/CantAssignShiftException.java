package employee.Exceptions;

public class CantAssignShiftException extends Exception{
    public CantAssignShiftException() {
        super();
    }

    // Constructor with message
    public CantAssignShiftException(String message) {
        super(message);
    }

}
