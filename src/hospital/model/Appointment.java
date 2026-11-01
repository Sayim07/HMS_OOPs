package hospital.model;

import hospital.exception.InvalidInputException;

/**
 * Class representing an Appointment between a Patient and a Doctor.
 * Demonstrates:
 * 1. Abstraction: implements Displayable.
 * 2. Encapsulation: controls appointment timing, identifiers, and status transitions.
 */
public class Appointment implements Displayable {
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String date; // Format: YYYY-MM-DD
    private String timeSlot; // Format: 09:00 AM, 11:30 AM, etc.
    private String reason;
    private String status; // "BOOKED", "COMPLETED", "CANCELLED"

    public Appointment(String appointmentId, String patientId, String doctorId,
                       String date, String timeSlot, String reason, String status) throws InvalidInputException {
        setAppointmentId(appointmentId);
        setPatientId(patientId);
        setDoctorId(doctorId);
        setDate(date);
        setTimeSlot(timeSlot);
        setReason(reason);
        setStatus(status);
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) throws InvalidInputException {
        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            throw new InvalidInputException("Appointment ID cannot be empty.");
        }
        this.appointmentId = appointmentId.trim().toUpperCase();
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) throws InvalidInputException {
        if (patientId == null || patientId.trim().isEmpty()) {
            throw new InvalidInputException("Patient ID cannot be empty.");
        }
        this.patientId = patientId.trim().toUpperCase();
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) throws InvalidInputException {
        if (doctorId == null || doctorId.trim().isEmpty()) {
            throw new InvalidInputException("Doctor ID cannot be empty.");
        }
        this.doctorId = doctorId.trim().toUpperCase();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) throws InvalidInputException {
        if (date == null || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new InvalidInputException("Date must be in YYYY-MM-DD format (e.g., 2026-11-05).");
        }
        this.date = date.trim();
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) throws InvalidInputException {
        if (timeSlot == null || timeSlot.trim().isEmpty()) {
            throw new InvalidInputException("Time slot cannot be empty.");
        }
        this.timeSlot = timeSlot.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) throws InvalidInputException {
        if (reason == null || reason.trim().isEmpty()) {
            throw new InvalidInputException("Reason for appointment cannot be empty.");
        }
        this.reason = reason.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) throws InvalidInputException {
        if (status == null || (!status.equalsIgnoreCase("BOOKED") &&
                               !status.equalsIgnoreCase("COMPLETED") &&
                               !status.equalsIgnoreCase("CANCELLED"))) {
            throw new InvalidInputException("Status must be BOOKED, COMPLETED, or CANCELLED.");
        }
        this.status = status.trim().toUpperCase();
    }

    @Override
    public void displayDetails() {
        System.out.println(getFormattedDetails());
    }

    @Override
    public String getFormattedDetails() {
        return String.format(
            "Appt ID: %-8s | Date: %-10s | Time: %-10s | Doctor: %-6s | Patient: %-6s | Status: %-9s | Reason: %s",
            appointmentId, date, timeSlot, doctorId, patientId, status, reason
        );
    }

    // Serialization helper for file handling: CSV delimiter '|'
    public String toDataString() {
        return String.join("|",
                appointmentId,
                patientId,
                doctorId,
                date,
                timeSlot,
                reason,
                status
        );
    }

    public static Appointment fromDataString(String line) throws InvalidInputException {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 7) {
            throw new InvalidInputException("Corrupted appointment record line: " + line);
        }
        return new Appointment(
                parts[0],
                parts[1],
                parts[2],
                parts[3],
                parts[4],
                parts[5],
                parts[6]
        );
    }
}

