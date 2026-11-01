package hospital.model;

import hospital.exception.InvalidInputException;

/**
 * Class representing a Doctor.
 * Demonstrates:
 * 1. Inheritance: extends Person.
 * 2. Polymorphism: overrides getRole() and getFormattedDetails().
 * 3. Encapsulation: encapsulates medical credentials and consulting charges.
 */
public class Doctor extends Person {
    private String specialization;
    private String qualification;
    private double consultationFee;
    private String department;
    private int experienceYears;

    public Doctor(String id, String name, int age, String gender, String contactNumber,
                  String specialization, String qualification, double consultationFee,
                  String department, int experienceYears) throws InvalidInputException {
        super(id, name, age, gender, contactNumber);
        setSpecialization(specialization);
        setQualification(qualification);
        setConsultationFee(consultationFee);
        setDepartment(department);
        setExperienceYears(experienceYears);
    }

    @Override
    public String getRole() {
        return "Doctor";
    }

    @Override
    public String getFormattedDetails() {
        return String.format(
            "ID: %-6s | Name: Dr. %-15s | Specialization: %-16s | Dept: %-12s | Exp: %2d yrs | Fee: $%.2f | Qual: %s",
            getId(), getName(), specialization, department, experienceYears, consultationFee, qualification
        );
    }

    // Getters and Setters
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) throws InvalidInputException {
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new InvalidInputException("Specialization cannot be empty.");
        }
        this.specialization = specialization.trim();
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) throws InvalidInputException {
        if (qualification == null || qualification.trim().isEmpty()) {
            throw new InvalidInputException("Qualification cannot be empty.");
        }
        this.qualification = qualification.trim();
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) throws InvalidInputException {
        if (consultationFee < 0) {
            throw new InvalidInputException("Consultation fee cannot be negative.");
        }
        this.consultationFee = consultationFee;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) throws InvalidInputException {
        if (department == null || department.trim().isEmpty()) {
            throw new InvalidInputException("Department cannot be empty.");
        }
        this.department = department.trim();
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) throws InvalidInputException {
        if (experienceYears < 0) {
            throw new InvalidInputException("Experience years cannot be negative.");
        }
        this.experienceYears = experienceYears;
    }

    // Serialization helper for file handling: CSV delimiter '|'
    public String toDataString() {
        return String.join("|",
                getId(),
                getName(),
                String.valueOf(getAge()),
                getGender(),
                getContactNumber(),
                specialization,
                qualification,
                String.valueOf(consultationFee),
                department,
                String.valueOf(experienceYears)
        );
    }

    public static Doctor fromDataString(String line) throws InvalidInputException {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 10) {
            throw new InvalidInputException("Corrupted doctor record line: " + line);
        }
        return new Doctor(
                parts[0],
                parts[1],
                Integer.parseInt(parts[2]),
                parts[3],
                parts[4],
                parts[5],
                parts[6],
                Double.parseDouble(parts[7]),
                parts[8],
                Integer.parseInt(parts[9])
        );
    }
}

