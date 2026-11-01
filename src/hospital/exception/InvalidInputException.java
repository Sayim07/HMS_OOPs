package hospital.exception;

/**
 * Thrown when data validation fails during object creation or modification.
 */
public class InvalidInputException extends HospitalException {
    public InvalidInputException(String message) {
        super("Validation Error: " + message);
    }
}

