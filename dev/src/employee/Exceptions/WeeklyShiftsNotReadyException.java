package employee.Exceptions;

public class WeeklyShiftsNotReadyException extends Exception{
    public WeeklyShiftsNotReadyException() {
        super();
    }

    // Constructor with message
    public WeeklyShiftsNotReadyException(String message) {
        super(message);
    }
}

