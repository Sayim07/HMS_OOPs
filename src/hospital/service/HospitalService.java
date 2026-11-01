package hospital.service;

import hospital.exception.AppointmentSlotUnavailableException;
import hospital.exception.DoctorNotFoundException;
import hospital.exception.HospitalException;
import hospital.exception.InvalidInputException;
import hospital.exception.PatientNotFoundException;
import hospital.model.Appointment;
import hospital.model.Doctor;
import hospital.model.Patient;
import hospital.util.FileManager;

import java.io.File;
import java.util.*;

/**
 * Service class managing hospital operations.
 * Demonstrates:
 * 1. Collections Framework:
 *    - Map (HashMap) for O(1) key-value lookup of Patients and Doctors by their unique ID.
 *    - List (ArrayList) for ordered storage and iteration of Appointments.
 *    - Set (TreeSet / HashSet) for maintaining unique hospital departments and detecting time-slot conflicts.
 * 2. Exception Handling: custom exceptions thrown for missing entities, duplicate IDs, or scheduling conflicts.
 * 3. File Handling integration: loads from and persists to file storage.
 * 4. Polymorphism and Method Overloading.
 */
public class HospitalService {
    // Collections
    private final Map<String, Patient> patientMap = new HashMap<>();
    private final Map<String, Doctor> doctorMap = new HashMap<>();
    private final List<Appointment> appointmentList = new ArrayList<>();
    private final Set<String> departments = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    // File paths
    private final String patientsFilePath = "data/patients.txt";
    private final String doctorsFilePath = "data/doctors.txt";
    private final String appointmentsFilePath = "data/appointments.txt";

    public HospitalService() {
        // Attempt to load existing records, or seed defaults if fresh
        try {
            loadAllData();
            if (doctorMap.isEmpty() && patientMap.isEmpty()) {
                seedInitialData();
            }
        } catch (HospitalException e) {
            System.err.println("Notice: Could not load existing data (" + e.getMessage() + "). Seeding sample records.");
            try {
                seedInitialData();
            } catch (Exception ex) {
                System.err.println("Failed to seed initial data: " + ex.getMessage());
            }
        }
    }

    // ==========================================
    // PATIENT OPERATIONS
    // ==========================================

    public void addPatient(Patient patient) throws InvalidInputException {
        if (patient == null) {
            throw new InvalidInputException("Patient details cannot be null.");
        }
        if (patientMap.containsKey(patient.getId())) {
            throw new InvalidInputException("Patient with ID " + patient.getId() + " already exists.");
        }
        patientMap.put(patient.getId(), patient);
    }

    public Patient getPatient(String patientId) throws PatientNotFoundException {
        Patient p = patientMap.get(patientId.toUpperCase());
        if (p == null) {
            throw new PatientNotFoundException(patientId);
        }
        return p;
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }

    // Method Overloading: Search by query (matches ID or Name)
    public List<Patient> searchPatients(String query) {
        String lowerQuery = query.toLowerCase();
        List<Patient> result = new ArrayList<>();
        for (Patient p : patientMap.values()) {
            if (p.getId().toLowerCase().contains(lowerQuery) || p.getName().toLowerCase().contains(lowerQuery)) {
                result.add(p);
            }
        }
        return result;
    }

    public boolean dischargePatient(String patientId) throws PatientNotFoundException {
        Patient p = getPatient(patientId);
        if (!p.isAdmitted()) {
            return false; // Already not admitted
        }
        p.setAdmitted(false);
        return true;
    }

    // ==========================================
    // DOCTOR OPERATIONS
    // ==========================================

    public void addDoctor(Doctor doctor) throws InvalidInputException {
        if (doctor == null) {
            throw new InvalidInputException("Doctor details cannot be null.");
        }
        if (doctorMap.containsKey(doctor.getId())) {
            throw new InvalidInputException("Doctor with ID " + doctor.getId() + " already exists.");
        }
        doctorMap.put(doctor.getId(), doctor);
        departments.add(doctor.getDepartment());
    }

    public Doctor getDoctor(String doctorId) throws DoctorNotFoundException {
        Doctor d = doctorMap.get(doctorId.toUpperCase());
        if (d == null) {
            throw new DoctorNotFoundException(doctorId);
        }
        return d;
    }

    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctorMap.values());
    }

    public Set<String> getAllDepartments() {
        return Collections.unmodifiableSet(departments);
    }

    // Method Overloading: Search by query (matches ID, Name, Specialization, or Dept)
    public List<Doctor> searchDoctors(String query) {
        String lowerQuery = query.toLowerCase();
        List<Doctor> result = new ArrayList<>();
        for (Doctor d : doctorMap.values()) {
            if (d.getId().toLowerCase().contains(lowerQuery) ||
                d.getName().toLowerCase().contains(lowerQuery) ||
                d.getSpecialization().toLowerCase().contains(lowerQuery) ||
                d.getDepartment().toLowerCase().contains(lowerQuery)) {
                result.add(d);
            }
        }
        return result;
    }

    // ==========================================
    // APPOINTMENT OPERATIONS
    // ==========================================

    public Appointment bookAppointment(String appointmentId, String patientId, String doctorId,
                                      String date, String timeSlot, String reason)
            throws HospitalException {
        // Verify patient exists
        getPatient(patientId);
        // Verify doctor exists
        getDoctor(doctorId);

        // Check if appointment ID already exists
        for (Appointment a : appointmentList) {
            if (a.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                throw new InvalidInputException("Appointment with ID " + appointmentId + " already exists.");
            }
        }

        // Check slot conflict for the doctor
        for (Appointment a : appointmentList) {
            if (a.getDoctorId().equalsIgnoreCase(doctorId) &&
                a.getDate().equalsIgnoreCase(date) &&
                a.getTimeSlot().equalsIgnoreCase(timeSlot) &&
                !a.getStatus().equalsIgnoreCase("CANCELLED")) {
                throw new AppointmentSlotUnavailableException(
                    "Doctor " + doctorId + " already has an appointment booked on " + date + " at " + timeSlot
                );
            }
        }

        Appointment newAppt = new Appointment(appointmentId, patientId, doctorId, date, timeSlot, reason, "BOOKED");
        appointmentList.add(newAppt);
        return newAppt;
    }

    public List<Appointment> getAllAppointments() {
        return Collections.unmodifiableList(appointmentList);
    }

    public List<Appointment> getAppointmentsForDoctor(String doctorId) {
        List<Appointment> list = new ArrayList<>();
        for (Appointment a : appointmentList) {
            if (a.getDoctorId().equalsIgnoreCase(doctorId)) {
                list.add(a);
            }
        }
        return list;
    }

    public List<Appointment> getAppointmentsForPatient(String patientId) {
        List<Appointment> list = new ArrayList<>();
        for (Appointment a : appointmentList) {
            if (a.getPatientId().equalsIgnoreCase(patientId)) {
                list.add(a);
            }
        }
        return list;
    }

    public boolean updateAppointmentStatus(String appointmentId, String newStatus) throws InvalidInputException {
        for (Appointment a : appointmentList) {
            if (a.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                a.setStatus(newStatus);
                return true;
            }
        }
        return false;
    }

    // ==========================================
    // BILLING & REPORTS
    // ==========================================

    public String generatePatientBill(String patientId) throws PatientNotFoundException {
        Patient p = getPatient(patientId);
        return p.generateInvoice();
    }

    public String saveBillToFile(String patientId) throws HospitalException {
        Patient p = getPatient(patientId);
        String invoice = p.generateInvoice();
        return FileManager.exportInvoiceToFile(invoice, patientId);
    }

    // ==========================================
    // FILE PERSISTENCE
    // ==========================================

    public void saveAllData() throws HospitalException {
        FileManager.savePatients(patientMap.values(), patientsFilePath);
        FileManager.saveDoctors(doctorMap.values(), doctorsFilePath);
        FileManager.saveAppointments(appointmentList, appointmentsFilePath);
    }

    public void loadAllData() throws HospitalException {
        patientMap.clear();
        doctorMap.clear();
        appointmentList.clear();
        departments.clear();

        List<Patient> loadedPatients = FileManager.loadPatients(patientsFilePath);
        for (Patient p : loadedPatients) {
            patientMap.put(p.getId(), p);
        }

        List<Doctor> loadedDoctors = FileManager.loadDoctors(doctorsFilePath);
        for (Doctor d : loadedDoctors) {
            doctorMap.put(d.getId(), d);
            departments.add(d.getDepartment());
        }

        List<Appointment> loadedAppointments = FileManager.loadAppointments(appointmentsFilePath);
        appointmentList.addAll(loadedAppointments);
    }

    /**
     * Seeds realistic sample records if running for the first time.
     */
    private void seedInitialData() throws InvalidInputException, HospitalException {
        // Sample Doctors
        addDoctor(new Doctor("DOC101", "Aarav Sharma", 45, "Male", "9876543210", "Cardiology", "MD, DM (Cardio)", 80.0, "Cardiology", 18));
        addDoctor(new Doctor("DOC102", "Priya Nair", 38, "Female", "9876543211", "Neurology", "MD, DM (Neuro)", 95.0, "Neurology", 12));
        addDoctor(new Doctor("DOC103", "Johnathan Miller", 50, "Male", "9876543212", "Orthopedics", "MS (Ortho)", 75.0, "Orthopedics", 22));
        addDoctor(new Doctor("DOC104", "Sunita Gupta", 34, "Female", "9876543213", "Pediatrics", "MD (Pediatrics)", 60.0, "Pediatrics", 8));

        // Sample Patients
        addPatient(new Patient("PAT101", "Rahul Verma", 29, "Male", "9123456780", "Chest Pain", "B+", "DOC101", "Deluxe", 3, 120.0, 85.0, 150.0, true));
        addPatient(new Patient("PAT102", "Ananya Singh", 24, "Female", "9123456781", "Migraine", "O+", "DOC102", "General", 1, 60.0, 45.0, 90.0, false));
        addPatient(new Patient("PAT103", "Michael Scott", 48, "Male", "9123456782", "Fractured Fibula", "A+", "DOC103", "General", 5, 60.0, 110.0, 200.0, true));

        // Sample Appointments
        try {
            bookAppointment("APT501", "PAT101", "DOC101", "2026-11-05", "10:00 AM", "Follow-up ECG consultation");
            bookAppointment("APT502", "PAT102", "DOC102", "2026-11-06", "11:30 AM", "Routine migraine assessment");
        } catch (HospitalException ignored) {}

        // Save seeded data to files
        saveAllData();
    }
}

