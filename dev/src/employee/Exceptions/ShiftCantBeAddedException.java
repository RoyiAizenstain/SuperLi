package employee.Exceptions;

public class ShiftCantBeAddedException extends Exception{
    public ShiftCantBeAddedException() {
        super();
    }

    // Constructor with message
    public ShiftCantBeAddedException(String message) {
        super(message);
    }
}

