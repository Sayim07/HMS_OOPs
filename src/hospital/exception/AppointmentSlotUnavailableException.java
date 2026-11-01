package hospital.exception;

/**
 * Thrown when an appointment slot is already booked or invalid.
 */
public class AppointmentSlotUnavailableException extends HospitalException {
    public AppointmentSlotUnavailableException(String message) {
        super("Appointment Slot Unavailable: " + message);
    }
}

