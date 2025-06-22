package employee.Exceptions;

public class ShiftHasManager extends Exception{
    public ShiftHasManager() {
        super();
    }

    // Constructor with message
    public ShiftHasManager(String message) {
        super(message);
    }
}


