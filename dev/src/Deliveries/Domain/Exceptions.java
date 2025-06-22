package Deliveries.Domain;

public class Exceptions {
    // Checked exceptions - must be caught or declared
    public static class DeliveryException extends Exception {
        public DeliveryException(String message) {
            super(message);
        }
    }

    // Unchecked exceptions - do not need to be caught or declared
    public static class InputNotFoundException extends DeliveryException {
        public InputNotFoundException(String message) {
            super(message);
        }
    }

    //delivery exceptions not completed
    public static class DeliveryNotCompletedException extends DeliveryException {
        public DeliveryNotCompletedException(String message) {
            super(message);
        }
    }

    // Order exceptions
    public static class OrderNotFoundException extends DeliveryException {
        public OrderNotFoundException(String orderId) {
            super("Order not found: " + orderId);
        }
    }

    // Order already exists
    public static class AlreadyExist extends DeliveryException {
        public AlreadyExist(String message) {
            super(message);
        }
    }



}