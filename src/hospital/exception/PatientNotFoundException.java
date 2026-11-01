package hospital.exception;

/**
 * Thrown when a patient with a specific ID is not found.
 */
public class PatientNotFoundException extends HospitalException {
    public PatientNotFoundException(String patientId) {
        super("Patient with ID '" + patientId + "' was not found in the records.");
    }
}

