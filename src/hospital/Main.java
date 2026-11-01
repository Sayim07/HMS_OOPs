package hospital;

import hospital.exception.HospitalException;
import hospital.model.Appointment;
import hospital.model.Doctor;
import hospital.model.Patient;
import hospital.service.HospitalService;
import hospital.util.InputHelper;

import java.util.List;
import java.util.Set;

/**
 * Main application runner for the Hospital Management System (HMS).
 * Demonstrates:
 * 1. Object-Oriented Programming (OOP) Principles:
 *    - Encapsulation: Private member data with validated setters/getters.
 *    - Inheritance: Base Person class inherited by Patient and Doctor.
 *    - Abstraction: Abstract Person class, Billable and Displayable interfaces.
 *    - Polymorphism: Overridden displayDetails(), calculateTotalBill(), and method overloading.
 * 2. Collections: HashMap, ArrayList, TreeSet.
 * 3. File Handling: Persistent text file storage and invoice report exporting.
 * 4. Exception Handling: Structured custom exceptions and input sanitization.
 */
public class Main {
    private static final HospitalService service = new HospitalService();

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("        WELCOME TO ADVANCED HOSPITAL MANAGEMENT SYSTEM (HMS)      ");
        System.out.println("==================================================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = InputHelper.readInt("Select an option (1-7): ");
            System.out.println();

            switch (choice) {
                case 1 -> handlePatientManagement();
                case 2 -> handleDoctorManagement();
                case 3 -> handleAppointmentManagement();
                case 4 -> handleBillingManagement();
                case 5 -> handleFileOperations();
                case 6 -> showOopArchitectureSummary();
                case 7 -> {
                    System.out.println("Saving all data before shutdown...");
                    try {
                        service.saveAllData();
                        System.out.println("All records saved successfully. Goodbye!");
                    } catch (HospitalException e) {
                        System.err.println("Warning: Could not save data: " + e.getMessage());
                    }
                    running = false;
                }
                default -> System.out.println("[!] Invalid choice. Please enter a number between 1 and 7.");
            }
            System.out.println();
        }
    }

    private static void printMainMenu() {
        System.out.println("------------------------- MAIN MENU -----------------------------");
        System.out.println(" 1. Patient Management  (Add, View, Search, Discharge)");
        System.out.println(" 2. Doctor Management   (Add, View, Search, Departments)");
        System.out.println(" 3. Appointments        (Book, View, Cancel, Schedule Check)");
        System.out.println(" 4. Billing & Invoicing (Calculate Bill, Export Invoice)");
        System.out.println(" 5. File Persistence    (Save to Disk, Reload from Disk)");
        System.out.println(" 6. View OOP Concepts   (System Architecture & Concept Map)");
        System.out.println(" 7. Exit System");
        System.out.println("-----------------------------------------------------------------");
    }

    // ==========================================
    // 1. PATIENT MANAGEMENT
    // ==========================================
    private static void handlePatientManagement() {
        System.out.println(">>> PATIENT MANAGEMENT SUBMENU <<<");
        System.out.println(" 1. Register New Patient");
        System.out.println(" 2. View All Patients");
        System.out.println(" 3. Search Patient (by ID or Name)");
        System.out.println(" 4. Discharge Patient");
        System.out.println(" 5. Back to Main Menu");

        int choice = InputHelper.readInt("Select an option (1-5): ");
        System.out.println();

        try {
            switch (choice) {
                case 1 -> registerNewPatient();
                case 2 -> viewAllPatients();
                case 3 -> searchPatients();
                case 4 -> dischargePatient();
                case 5 -> {}
                default -> System.out.println("[!] Invalid choice.");
            }
        } catch (HospitalException e) {
            System.err.println("[Error] " + e.getMessage());
        }
    }

    private static void registerNewPatient() throws HospitalException {
        System.out.println("--- Register New Patient ---");
        String id = InputHelper.readString("Enter Patient ID (e.g., PAT104): ");
        String name = InputHelper.readString("Enter Patient Name: ");
        int age = InputHelper.readInt("Enter Age: ");
        String gender = InputHelper.readString("Enter Gender (Male/Female/Other): ");
        String contact = InputHelper.readString("Enter Contact Number: ");
        String disease = InputHelper.readString("Enter Disease / Diagnosis: ");
        String blood = InputHelper.readString("Enter Blood Group (e.g. O+, A-, B+): ");
        String doctorId = InputHelper.readOptionalString("Enter Assigned Doctor ID (or press Enter if none): ", "NONE");
        String roomType = InputHelper.readString("Enter Room Type (General/ICU/Deluxe/Outpatient): ");
        boolean isAdmitted = InputHelper.readBoolean("Is the patient currently admitted?");
        int days = 0;
        double dailyRate = 0.0;
        if (isAdmitted) {
            days = InputHelper.readInt("Enter Days Admitted: ");
            dailyRate = InputHelper.readDouble("Enter Daily Room Rate ($): ");
        }
        double medCharges = InputHelper.readDouble("Enter Medicine Charges ($): ");
        double labCharges = InputHelper.readDouble("Enter Lab Charges ($): ");

        Patient patient = new Patient(id, name, age, gender, contact, disease, blood,
                doctorId, roomType, days, dailyRate, medCharges, labCharges, isAdmitted);
        service.addPatient(patient);
        System.out.println("[Success] Patient registered successfully: " + patient.getName());
    }

    private static void viewAllPatients() {
        List<Patient> list = service.getAllPatients();
        if (list.isEmpty()) {
            System.out.println("No patients currently on record.");
            return;
        }
        System.out.println("--- All Registered Patients (" + list.size() + ") ---");
        for (Patient p : list) {
            p.displayDetails();
        }
    }

    private static void searchPatients() {
        String query = InputHelper.readString("Enter search keyword (ID or Name): ");
        List<Patient> results = service.searchPatients(query);
        if (results.isEmpty()) {
            System.out.println("No patients found matching query: " + query);
        } else {
            System.out.println("Found " + results.size() + " matching patient(s):");
            for (Patient p : results) {
                p.displayDetails();
            }
        }
    }

    private static void dischargePatient() throws HospitalException {
        String patientId = InputHelper.readString("Enter Patient ID to discharge: ");
        boolean discharged = service.dischargePatient(patientId);
        if (discharged) {
            System.out.println("[Success] Patient " + patientId + " marked as discharged.");
        } else {
            System.out.println("[Info] Patient " + patientId + " was already marked as not admitted.");
        }
    }

    // ==========================================
    // 2. DOCTOR MANAGEMENT
    // ==========================================
    private static void handleDoctorManagement() {
        System.out.println(">>> DOCTOR MANAGEMENT SUBMENU <<<");
        System.out.println(" 1. Register New Doctor");
        System.out.println(" 2. View All Doctors");
        System.out.println(" 3. Search Doctor (by Name, Specialization, Dept)");
        System.out.println(" 4. View All Departments");
        System.out.println(" 5. Back to Main Menu");

        int choice = InputHelper.readInt("Select an option (1-5): ");
        System.out.println();

        try {
            switch (choice) {
                case 1 -> registerNewDoctor();
                case 2 -> viewAllDoctors();
                case 3 -> searchDoctors();
                case 4 -> viewAllDepartments();
                case 5 -> {}
                default -> System.out.println("[!] Invalid choice.");
            }
        } catch (HospitalException e) {
            System.err.println("[Error] " + e.getMessage());
        }
    }

    private static void registerNewDoctor() throws HospitalException {
        System.out.println("--- Register New Doctor ---");
        String id = InputHelper.readString("Enter Doctor ID (e.g., DOC105): ");
        String name = InputHelper.readString("Enter Doctor Name: ");
        int age = InputHelper.readInt("Enter Age: ");
        String gender = InputHelper.readString("Enter Gender (Male/Female/Other): ");
        String contact = InputHelper.readString("Enter Contact Number: ");
        String specialization = InputHelper.readString("Enter Specialization (e.g., Cardiology, Oncology): ");
        String qualification = InputHelper.readString("Enter Qualification (e.g., MD, MS, MBBS): ");
        double fee = InputHelper.readDouble("Enter Consultation Fee ($): ");
        String department = InputHelper.readString("Enter Department: ");
        int expYears = InputHelper.readInt("Enter Years of Experience: ");

        Doctor doctor = new Doctor(id, name, age, gender, contact, specialization, qualification, fee, department, expYears);
        service.addDoctor(doctor);
        System.out.println("[Success] Doctor registered successfully: Dr. " + doctor.getName());
    }

    private static void viewAllDoctors() {
        List<Doctor> list = service.getAllDoctors();
        if (list.isEmpty()) {
            System.out.println("No doctors currently on record.");
            return;
        }
        System.out.println("--- All Hospital Doctors (" + list.size() + ") ---");
        for (Doctor d : list) {
            d.displayDetails();
        }
    }

    private static void searchDoctors() {
        String query = InputHelper.readString("Enter search keyword (ID, Name, Specialization, or Dept): ");
        List<Doctor> results = service.searchDoctors(query);
        if (results.isEmpty()) {
            System.out.println("No doctors found matching query: " + query);
        } else {
            System.out.println("Found " + results.size() + " matching doctor(s):");
            for (Doctor d : results) {
                d.displayDetails();
            }
        }
    }

    private static void viewAllDepartments() {
        Set<String> depts = service.getAllDepartments();
        System.out.println("--- Hospital Departments (" + depts.size() + ") ---");
        if (depts.isEmpty()) {
            System.out.println("No departments recorded.");
        } else {
            for (String dept : depts) {
                System.out.println(" - " + dept);
            }
        }
    }

    // ==========================================
    // 3. APPOINTMENT MANAGEMENT
    // ==========================================
    private static void handleAppointmentManagement() {
        System.out.println(">>> APPOINTMENT MANAGEMENT SUBMENU <<<");
        System.out.println(" 1. Book New Appointment");
        System.out.println(" 2. View All Appointments");
        System.out.println(" 3. View Appointments for a Doctor");
        System.out.println(" 4. View Appointments for a Patient");
        System.out.println(" 5. Update Appointment Status (COMPLETED / CANCELLED)");
        System.out.println(" 6. Back to Main Menu");

        int choice = InputHelper.readInt("Select an option (1-6): ");
        System.out.println();

        try {
            switch (choice) {
                case 1 -> bookNewAppointment();
                case 2 -> viewAllAppointments();
                case 3 -> viewAppointmentsByDoctor();
                case 4 -> viewAppointmentsByPatient();
                case 5 -> updateAppointmentStatus();
                case 6 -> {}
                default -> System.out.println("[!] Invalid choice.");
            }
        } catch (HospitalException e) {
            System.err.println("[Error] " + e.getMessage());
        }
    }

    private static void bookNewAppointment() throws HospitalException {
        System.out.println("--- Book New Appointment ---");
        String apptId = InputHelper.readString("Enter Appointment ID (e.g. APT601): ");
        String patientId = InputHelper.readString("Enter Patient ID (e.g. PAT101): ");
        String doctorId = InputHelper.readString("Enter Doctor ID (e.g. DOC101): ");
        String date = InputHelper.readString("Enter Date (YYYY-MM-DD): ");
        String slot = InputHelper.readString("Enter Time Slot (e.g. 10:00 AM, 02:00 PM): ");
        String reason = InputHelper.readString("Enter Reason for Appointment: ");

        Appointment appt = service.bookAppointment(apptId, patientId, doctorId, date, slot, reason);
        System.out.println("[Success] Appointment confirmed!");
        appt.displayDetails();
    }

    private static void viewAllAppointments() {
        List<Appointment> list = service.getAllAppointments();
        if (list.isEmpty()) {
            System.out.println("No appointments booked yet.");
            return;
        }
        System.out.println("--- All Appointments (" + list.size() + ") ---");
        for (Appointment a : list) {
            a.displayDetails();
        }
    }

    private static void viewAppointmentsByDoctor() {
        String docId = InputHelper.readString("Enter Doctor ID: ");
        List<Appointment> list = service.getAppointmentsForDoctor(docId);
        if (list.isEmpty()) {
            System.out.println("No appointments scheduled for Doctor " + docId);
        } else {
            System.out.println("--- Appointments for Doctor " + docId + " ---");
            for (Appointment a : list) {
                a.displayDetails();
            }
        }
    }

    private static void viewAppointmentsByPatient() {
        String patId = InputHelper.readString("Enter Patient ID: ");
        List<Appointment> list = service.getAppointmentsForPatient(patId);
        if (list.isEmpty()) {
            System.out.println("No appointments found for Patient " + patId);
        } else {
            System.out.println("--- Appointments for Patient " + patId + " ---");
            for (Appointment a : list) {
                a.displayDetails();
            }
        }
    }

    private static void updateAppointmentStatus() throws HospitalException {
        String apptId = InputHelper.readString("Enter Appointment ID: ");
        String status = InputHelper.readString("Enter New Status (BOOKED / COMPLETED / CANCELLED): ");
        boolean updated = service.updateAppointmentStatus(apptId, status);
        if (updated) {
            System.out.println("[Success] Appointment status updated to " + status.toUpperCase());
        } else {
            System.out.println("[!] Appointment not found with ID: " + apptId);
        }
    }

    // ==========================================
    // 4. BILLING & INVOICING
    // ==========================================
    private static void handleBillingManagement() {
        System.out.println(">>> BILLING & INVOICING SUBMENU <<<");
        System.out.println(" 1. View Patient Invoice");
        System.out.println(" 2. Export Invoice to File (Text Report)");
        System.out.println(" 3. Back to Main Menu");

        int choice = InputHelper.readInt("Select an option (1-3): ");
        System.out.println();

        try {
            switch (choice) {
                case 1 -> {
                    String patId = InputHelper.readString("Enter Patient ID: ");
                    String invoice = service.generatePatientBill(patId);
                    System.out.println(invoice);
                }
                case 2 -> {
                    String patId = InputHelper.readString("Enter Patient ID: ");
                    String filePath = service.saveBillToFile(patId);
                    System.out.println("[Success] Bill exported to file: " + filePath);
                }
                case 3 -> {}
                default -> System.out.println("[!] Invalid choice.");
            }
        } catch (HospitalException e) {
            System.err.println("[Error] " + e.getMessage());
        }
    }

    // ==========================================
    // 5. FILE OPERATIONS
    // ==========================================
    private static void handleFileOperations() {
        System.out.println(">>> FILE PERSISTENCE SUBMENU <<<");
        System.out.println(" 1. Save All Records to Disk");
        System.out.println(" 2. Reload All Records from Disk");
        System.out.println(" 3. Back to Main Menu");

        int choice = InputHelper.readInt("Select an option (1-3): ");
        System.out.println();

        try {
            switch (choice) {
                case 1 -> {
                    service.saveAllData();
                    System.out.println("[Success] Patients, doctors, and appointments saved successfully to 'data/' folder.");
                }
                case 2 -> {
                    service.loadAllData();
                    System.out.println("[Success] All records reloaded from files in 'data/' folder.");
                }
                case 3 -> {}
                default -> System.out.println("[!] Invalid choice.");
            }
        } catch (HospitalException e) {
            System.err.println("[Error] File operation failed: " + e.getMessage());
        }
    }

    // ==========================================
    // 6. OOP CONCEPTS OVERVIEW
    // ==========================================
    private static void showOopArchitectureSummary() {
        System.out.println("==========================================================================");
        System.out.println("                 OOP CONCEPTS MAPPING IN THIS PROJECT                     ");
        System.out.println("==========================================================================");
        System.out.println("1. ENCAPSULATION:");
        System.out.println("   - Private fields across Person, Patient, Doctor, Appointment.");
        System.out.println("   - Public getters & validated setters enforcing domain constraints.");
        System.out.println("2. INHERITANCE:");
        System.out.println("   - Person (Abstract Base Class) -> Patient, Doctor.");
        System.out.println("   - Code reusability for common demographic fields (id, name, age, contact).");
        System.out.println("3. ABSTRACTION:");
        System.out.println("   - Abstract class Person with abstract method getRole().");
        System.out.println("   - Interface 'Displayable' enforces consistent detail formatting.");
        System.out.println("   - Interface 'Billable' defines calculateTotalBill() and generateInvoice().");
        System.out.println("4. POLYMORPHISM:");
        System.out.println("   - Overriding: displayDetails(), getRole(), calculateTotalBill().");
        System.out.println("   - Overloading: searchPatients(query), searchDoctors(query).");
        System.out.println("5. COLLECTIONS FRAMEWORK:");
        System.out.println("   - HashMap<String, Patient> and HashMap<String, Doctor> for O(1) ID lookups.");
        System.out.println("   - ArrayList<Appointment> for ordered appointment scheduling.");
        System.out.println("   - TreeSet<String> for sorted unique medical departments.");
        System.out.println("6. FILE HANDLING:");
        System.out.println("   - BufferedReader & BufferedWriter with Try-With-Resources.");
        System.out.println("   - Persistent data stores in 'data/patients.txt', 'doctors.txt', 'appointments.txt'.");
        System.out.println("   - Generated text invoice reports in 'reports/'.");
        System.out.println("7. EXCEPTION HANDLING:");
        System.out.println("   - Custom hierarchy: HospitalException -> PatientNotFoundException,");
        System.out.println("     DoctorNotFoundException, AppointmentSlotUnavailableException, InvalidInputException.");
        System.out.println("   - Try-catch-finally blocks ensuring safe IO and input parsing.");
        System.out.println("==========================================================================");
    }
}

