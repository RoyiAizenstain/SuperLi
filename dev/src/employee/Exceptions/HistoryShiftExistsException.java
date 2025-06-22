package employee.Exceptions;

public class HistoryShiftExistsException extends Exception{
    // Basic constructor
    public HistoryShiftExistsException() {
        super();
    }

    // Constructor with message
    public HistoryShiftExistsException(String message) {
        super(message);
    }

}

