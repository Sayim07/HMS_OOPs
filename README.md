# Hospital Management System (HMS)

A modular, robust, and academic-grade **Hospital Management System** implemented in **Java (JDK 17+)**, designed specifically to demonstrate core **Object-Oriented Programming (OOP)** concepts, **Collections Framework**, **File Handling**, and **Custom Exception Handling**.

---

## 🏛️ OOP Concepts Implementation Matrix

| OOP / Java Concept | Implementation in Code | Key Files |
| :--- | :--- | :--- |
| **Classes & Objects** | Real-world entities modeled as classes (`Patient`, `Doctor`, `Appointment`, etc.) instantiated as objects. | `src/hospital/model/*.java` |
| **Encapsulation** | All member variables are `private`. Access is granted strictly via `public` getters and validated setters enforcing business rules (e.g., age boundaries, non-empty IDs). | [`Person.java`](file:///s:/sayim/Sem5/OOPs/HMS/src/hospital/model/Person.java), [`Patient.java`](file:///s:/sayim/Sem5/OOPs/HMS/src/hospital/model/Patient.java), [`Doctor.java`](file:///s:/sayim/Sem5/OOPs/HMS/src/hospital/model/Doctor.java) |
| **Inheritance** | Single-table/class hierarchy where `Patient` and `Doctor` inherit demographic and contact properties from `Person` via `extends Person`. | [`Patient.java`](file:///s:/sayim/Sem5/OOPs/HMS/src/hospital/model/Patient.java), [`Doctor.java`](file:///s:/sayim/Sem5/OOPs/HMS/src/hospital/model/Doctor.java) |
| **Abstraction** | 1. `abstract class Person` defines the abstract method `public abstract String getRole()`.<br>2. `interface Billable` defines billing contracts (`calculateTotalBill()`, `generateInvoice()`).<br>3. `interface Displayable` defines formatting contracts (`displayDetails()`). | [`Person.java`](file:///s:/sayim/Sem5/OOPs/HMS/src/hospital/model/Person.java), [`Billable.java`](file:///s:/sayim/Sem5/OOPs/HMS/src/hospital/model/Billable.java), [`Displayable.java`](file:///s:/sayim/Sem5/OOPs/HMS/src/hospital/model/Displayable.java) |
| **Polymorphism** | 1. **Runtime (Method Overriding)**: `displayDetails()` and `getRole()` overridden in `Doctor` and `Patient`.<br>2. **Compile-time (Method Overloading)**: Overloaded search functions `searchPatients(query)` and `searchDoctors(query)`. | [`HospitalService.java`](file:///s:/sayim/Sem5/OOPs/HMS/src/hospital/service/HospitalService.java), [`Patient.java`](file:///s:/sayim/Sem5/OOPs/HMS/src/hospital/model/Patient.java), [`Doctor.java`](file:///s:/sayim/Sem5/OOPs/HMS/src/hospital/model/Doctor.java) |
| **Collections** | 1. `Map<String, Patient>` (`HashMap`) for $O(1)$ fast ID lookup.<br>2. `Map<String, Doctor>` (`HashMap`) for doctor lookup.<br>3. `List<Appointment>` (`ArrayList`) for chronological appointments.<br>4. `Set<String>` (`TreeSet`) for sorted unique hospital departments. | [`HospitalService.java`](file:///s:/sayim/Sem5/OOPs/HMS/src/hospital/service/HospitalService.java) |
| **File Handling** | 1. `BufferedReader` and `BufferedWriter` with Java 7+ `try-with-resources`.<br>2. Persistent storage for patients, doctors, and appointments in `data/`.<br>3. Automatic generation and export of patient invoices to disk (`reports/Invoice_*.txt`). | [`FileManager.java`](file:///s:/sayim/Sem5/OOPs/HMS/src/hospital/util/FileManager.java) |
| **Exception Handling** | 1. Checked exception hierarchy rooted in `HospitalException`.<br>2. Custom subclasses: `PatientNotFoundException`, `DoctorNotFoundException`, `AppointmentSlotUnavailableException`, `InvalidInputException`.<br>3. `try-catch-finally` handling for user input and I/O. | `src/hospital/exception/*.java` |

---

## 📂 Project Directory Structure

```text
HMS/
├── bin/                          # Compiled .class bytecode
├── data/                         # Persistent text data files
│   ├── appointments.txt          # Saved appointments
│   ├── doctors.txt               # Doctor records
│   └── patients.txt              # Patient records
├── reports/                      # Exported billing receipts / invoices
│   └── Invoice_PAT101_*.txt
├── src/
│   └── hospital/
│       ├── Main.java             # Entry point & interactive CLI menu
│       ├── exception/            # Custom exception classes
│       │   ├── AppointmentSlotUnavailableException.java
│       │   ├── DoctorNotFoundException.java
│       │   ├── HospitalException.java
│       │   ├── InvalidInputException.java
│       │   └── PatientNotFoundException.java
│       ├── model/                # Domain models & interfaces
│       │   ├── Appointment.java
│       │   ├── Billable.java
│       │   ├── Displayable.java
│       │   ├── Doctor.java
│       │   ├── Patient.java
│       │   └── Person.java
│       ├── service/              # Core business logic & collections
│       │   └── HospitalService.java
│       └── util/                 # File I/O & safe console input
│           ├── FileManager.java
│           └── InputHelper.java
└── README.md
```

---

## 🚀 How to Compile and Run

### 1. Compile the Source Code
From the project root directory (`HMS`):
```powershell
javac -d bin src/hospital/*.java src/hospital/model/*.java src/hospital/exception/*.java src/hospital/service/*.java src/hospital/util/*.java
```

### 2. Run the Application
```powershell
java -cp bin hospital.Main
```

---

## 📋 Features & Functionalities

1. **Patient Management**:
   - Register new inpatients and outpatients with room classification.
   - Display all active and past patients.
   - Search patients by ID or name.
   - Discharge admitted patients.

2. **Doctor Management**:
   - Register new doctors with specialization, department, and consultation fees.
   - Search doctors by name, specialty, or department.
   - View all registered hospital medical departments.

3. **Appointment Scheduling**:
   - Schedule appointments linking patients to doctors with specific dates and time slots.
   - Automatic slot collision detection (prevents double-booking).
   - Filter appointments by specific doctor or patient.
   - Update appointment status (`BOOKED`, `COMPLETED`, `CANCELLED`).

4. **Billing & Invoicing**:
   - Dynamic calculation of inpatient bills: `(daysAdmitted * dailyRoomRate) + medicineCharges + labCharges`.
   - Formatted invoice generation on console.
   - Disk export to `reports/Invoice_<PATIENT_ID>_<TIMESTAMP>.txt`.

5. **Data Persistence**:
   - Automatically loads existing records on startup.
   - Seeds sample data if starting with a clean directory.
   - Auto-saves all changes on exit or on demand.

