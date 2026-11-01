package hospital.util;

import hospital.exception.HospitalException;
import hospital.exception.InvalidInputException;
import hospital.model.Appointment;
import hospital.model.Doctor;
import hospital.model.Patient;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility class demonstrating File Handling in Java:
 * 1. Byte and character streams with BufferedReader and BufferedWriter.
 * 2. Try-with-resources for automatic resource closing.
 * 3. Exception handling for IO and malformed data parsing.
 * 4. Writing formatted medical and billing reports to disk.
 */
public class FileManager {

    /**
     * Saves all patients to the specified file.
     */
    public static void savePatients(Collection<Patient> patients, String filePath) throws HospitalException {
        ensureFileExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Patient p : patients) {
                writer.write(p.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new HospitalException("Failed to save patients to file: " + filePath, e);
        }
    }

    /**
     * Loads patients from the specified file.
     */
    public static List<Patient> loadPatients(String filePath) throws HospitalException {
        List<Patient> patients = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return patients;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                try {
                    Patient p = Patient.fromDataString(line);
                    patients.add(p);
                } catch (InvalidInputException e) {
                    System.err.println("[Warning] Skipping corrupted patient line " + lineNum + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new HospitalException("Error reading patient data from: " + filePath, e);
        }
        return patients;
    }

    /**
     * Saves all doctors to the specified file.
     */
    public static void saveDoctors(Collection<Doctor> doctors, String filePath) throws HospitalException {
        ensureFileExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Doctor d : doctors) {
                writer.write(d.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new HospitalException("Failed to save doctors to file: " + filePath, e);
        }
    }

    /**
     * Loads doctors from the specified file.
     */
    public static List<Doctor> loadDoctors(String filePath) throws HospitalException {
        List<Doctor> doctors = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return doctors;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                try {
                    Doctor d = Doctor.fromDataString(line);
                    doctors.add(d);
                } catch (InvalidInputException e) {
                    System.err.println("[Warning] Skipping corrupted doctor line " + lineNum + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new HospitalException("Error reading doctor data from: " + filePath, e);
        }
        return doctors;
    }

    /**
     * Saves all appointments to the specified file.
     */
    public static void saveAppointments(Collection<Appointment> appointments, String filePath) throws HospitalException {
        ensureFileExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Appointment a : appointments) {
                writer.write(a.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new HospitalException("Failed to save appointments to file: " + filePath, e);
        }
    }

    /**
     * Loads appointments from the specified file.
     */
    public static List<Appointment> loadAppointments(String filePath) throws HospitalException {
        List<Appointment> appointments = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return appointments;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                try {
                    Appointment a = Appointment.fromDataString(line);
                    appointments.add(a);
                } catch (InvalidInputException e) {
                    System.err.println("[Warning] Skipping corrupted appointment line " + lineNum + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new HospitalException("Error reading appointment data from: " + filePath, e);
        }
        return appointments;
    }

    /**
     * Exports a patient invoice to a text file in reports/ folder.
     */
    public static String exportInvoiceToFile(String invoiceText, String patientId) throws HospitalException {
        String dirPath = "reports";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = dirPath + File.separator + "Invoice_" + patientId + "_" + System.currentTimeMillis() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(invoiceText);
        } catch (IOException e) {
            throw new HospitalException("Failed to write invoice to disk: " + fileName, e);
        }
        return fileName;
    }

    private static void ensureFileExists(String filePath) throws HospitalException {
        try {
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new HospitalException("Could not initialize file on disk: " + filePath, e);
        }
    }
}

