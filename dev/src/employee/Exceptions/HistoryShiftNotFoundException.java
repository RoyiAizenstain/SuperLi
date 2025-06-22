package employee.Exceptions;

public class HistoryShiftNotFoundException extends Exception{
    // Basic constructor
    public HistoryShiftNotFoundException() {
        super();
    }

    // Constructor with message
    public HistoryShiftNotFoundException(String message) {
        super(message);
    }
}
