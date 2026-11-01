package hospital.exception;

/**
 * Thrown when a doctor with a specific ID is not found.
 */
public class DoctorNotFoundException extends HospitalException {
    public DoctorNotFoundException(String doctorId) {
        super("Doctor with ID '" + doctorId + "' was not found in the records.");
    }
}

