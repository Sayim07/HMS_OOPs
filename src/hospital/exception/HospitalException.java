package hospital.exception;

/**
 * Base custom exception for the Hospital Management System.
 * Demonstrates Exception Handling hierarchy.
 */
public class HospitalException extends Exception {
    public HospitalException(String message) {
        super(message);
    }

    public HospitalException(String message, Throwable cause) {
        super(message, cause);
    }
}

