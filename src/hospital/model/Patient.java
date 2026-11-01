package hospital.model;

import hospital.exception.InvalidInputException;

/**
 * Class representing a Patient.
 * Demonstrates:
 * 1. Inheritance: extends Person.
 * 2. Abstraction: implements Billable and overrides Person's abstract methods.
 * 3. Encapsulation: private attributes with controlled access via getters and setters.
 */
public class Patient extends Person implements Billable {
    private String disease;
    private String bloodGroup;
    private String assignedDoctorId;
    private String roomType; // "General", "ICU", "Deluxe", "Outpatient"
    private int daysAdmitted;
    private double dailyRoomRate;
    private double medicineCharges;
    private double labCharges;
    private boolean isAdmitted;

    public Patient(String id, String name, int age, String gender, String contactNumber,
                   String disease, String bloodGroup, String assignedDoctorId,
                   String roomType, int daysAdmitted, double dailyRoomRate,
                   double medicineCharges, double labCharges, boolean isAdmitted) throws InvalidInputException {
        super(id, name, age, gender, contactNumber);
        setDisease(disease);
        setBloodGroup(bloodGroup);
        this.assignedDoctorId = (assignedDoctorId == null || assignedDoctorId.trim().isEmpty()) ? "NONE" : assignedDoctorId.trim().toUpperCase();
        setRoomType(roomType);
        setDaysAdmitted(daysAdmitted);
        setDailyRoomRate(dailyRoomRate);
        setMedicineCharges(medicineCharges);
        setLabCharges(labCharges);
        this.isAdmitted = isAdmitted;
    }

    @Override
    public String getRole() {
        return "Patient";
    }

    // Billable implementation
    @Override
    public double calculateTotalBill() {
        double roomTotal = isAdmitted ? (daysAdmitted * dailyRoomRate) : 0.0;
        return roomTotal + medicineCharges + labCharges;
    }

    @Override
    public String generateInvoice() {
        StringBuilder sb = new StringBuilder();
        sb.append("=====================================================\n");
        sb.append("               HOSPITAL BILLING INVOICE              \n");
        sb.append("=====================================================\n");
        sb.append(String.format("Patient ID     : %s\n", getId()));
        sb.append(String.format("Patient Name   : %s\n", getName()));
        sb.append(String.format("Age / Gender   : %d / %s\n", getAge(), getGender()));
        sb.append(String.format("Disease/Issue  : %s\n", disease));
        sb.append(String.format("Blood Group    : %s\n", bloodGroup));
        sb.append(String.format("Assigned Doctor: %s\n", assignedDoctorId));
        sb.append(String.format("Admission Status: %s\n", isAdmitted ? "Admitted" : "Outpatient / Discharged"));
        sb.append("-----------------------------------------------------\n");
        sb.append(String.format("Room Type      : %s\n", roomType));
        sb.append(String.format("Days Admitted  : %d day(s) @ $%.2f/day = $%.2f\n", 
                daysAdmitted, dailyRoomRate, (daysAdmitted * dailyRoomRate)));
        sb.append(String.format("Pharmacy/Meds  : $%.2f\n", medicineCharges));
        sb.append(String.format("Lab & Tests    : $%.2f\n", labCharges));
        sb.append("-----------------------------------------------------\n");
        sb.append(String.format("TOTAL AMOUNT   : $%.2f\n", calculateTotalBill()));
        sb.append("=====================================================\n");
        return sb.toString();
    }

    @Override
    public String getFormattedDetails() {
        return String.format(
            "ID: %-6s | Name: %-15s | Age: %-3d | Gender: %-6s | Disease: %-12s | Blood: %-4s | Doctor: %-6s | Admitted: %-5s | Total Bill: $%.2f",
            getId(), getName(), getAge(), getGender(), disease, bloodGroup, assignedDoctorId, (isAdmitted ? "Yes" : "No"), calculateTotalBill()
        );
    }

    // Getters and Setters
    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) throws InvalidInputException {
        if (disease == null || disease.trim().isEmpty()) {
            throw new InvalidInputException("Disease description cannot be empty.");
        }
        this.disease = disease.trim();
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) throws InvalidInputException {
        if (bloodGroup == null || bloodGroup.trim().isEmpty()) {
            throw new InvalidInputException("Blood group cannot be empty.");
        }
        this.bloodGroup = bloodGroup.trim().toUpperCase();
    }

    public String getAssignedDoctorId() {
        return assignedDoctorId;
    }

    public void setAssignedDoctorId(String assignedDoctorId) {
        this.assignedDoctorId = (assignedDoctorId == null || assignedDoctorId.trim().isEmpty()) ? "NONE" : assignedDoctorId.trim().toUpperCase();
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = (roomType == null || roomType.trim().isEmpty()) ? "General" : roomType.trim();
    }

    public int getDaysAdmitted() {
        return daysAdmitted;
    }

    public void setDaysAdmitted(int daysAdmitted) throws InvalidInputException {
        if (daysAdmitted < 0) {
            throw new InvalidInputException("Days admitted cannot be negative.");
        }
        this.daysAdmitted = daysAdmitted;
    }

    public double getDailyRoomRate() {
        return dailyRoomRate;
    }

    public void setDailyRoomRate(double dailyRoomRate) throws InvalidInputException {
        if (dailyRoomRate < 0) {
            throw new InvalidInputException("Daily room rate cannot be negative.");
        }
        this.dailyRoomRate = dailyRoomRate;
    }

    public double getMedicineCharges() {
        return medicineCharges;
    }

    public void setMedicineCharges(double medicineCharges) throws InvalidInputException {
        if (medicineCharges < 0) {
            throw new InvalidInputException("Medicine charges cannot be negative.");
        }
        this.medicineCharges = medicineCharges;
    }

    public double getLabCharges() {
        return labCharges;
    }

    public void setLabCharges(double labCharges) throws InvalidInputException {
        if (labCharges < 0) {
            throw new InvalidInputException("Lab charges cannot be negative.");
        }
        this.labCharges = labCharges;
    }

    public boolean isAdmitted() {
        return isAdmitted;
    }

    public void setAdmitted(boolean admitted) {
        isAdmitted = admitted;
    }

    // Serialization helper for file handling: CSV delimiter '|'
    public String toDataString() {
        return String.join("|",
                getId(),
                getName(),
                String.valueOf(getAge()),
                getGender(),
                getContactNumber(),
                disease,
                bloodGroup,
                assignedDoctorId,
                roomType,
                String.valueOf(daysAdmitted),
                String.valueOf(dailyRoomRate),
                String.valueOf(medicineCharges),
                String.valueOf(labCharges),
                String.valueOf(isAdmitted)
        );
    }

    public static Patient fromDataString(String line) throws InvalidInputException {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 14) {
            throw new InvalidInputException("Corrupted patient record line: " + line);
        }
        return new Patient(
                parts[0],
                parts[1],
                Integer.parseInt(parts[2]),
                parts[3],
                parts[4],
                parts[5],
                parts[6],
                parts[7],
                parts[8],
                Integer.parseInt(parts[9]),
                Double.parseDouble(parts[10]),
                Double.parseDouble(parts[11]),
                Double.parseDouble(parts[12]),
                Boolean.parseBoolean(parts[13])
        );
    }
}

