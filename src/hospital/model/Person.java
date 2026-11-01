package hospital.model;

import hospital.exception.InvalidInputException;

/**
 * Abstract Class representing a generic Person in the Hospital System.
 * Demonstrates:
 * 1. Abstraction: abstract methods and cannot be directly instantiated.
 * 2. Encapsulation: private member variables with public getter/setter methods and data validation.
 */
public abstract class Person implements Displayable {
    private String id;
    private String name;
    private int age;
    private String gender;
    private String contactNumber;

    public Person(String id, String name, int age, String gender, String contactNumber) throws InvalidInputException {
        setId(id);
        setName(name);
        setAge(age);
        setGender(gender);
        setContactNumber(contactNumber);
    }

    // Abstract method to be implemented by child classes (Doctor, Patient, etc.)
    public abstract String getRole();

    // Getters and Setters with Encapsulation & Validation
    public String getId() {
        return id;
    }

    public void setId(String id) throws InvalidInputException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidInputException("ID cannot be null or empty.");
        }
        this.id = id.trim().toUpperCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name cannot be null or empty.");
        }
        this.name = name.trim();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) throws InvalidInputException {
        if (age <= 0 || age > 130) {
            throw new InvalidInputException("Age must be between 1 and 130.");
        }
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) throws InvalidInputException {
        if (gender == null || gender.trim().isEmpty()) {
            throw new InvalidInputException("Gender cannot be null or empty.");
        }
        this.gender = gender.trim();
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) throws InvalidInputException {
        if (contactNumber == null || contactNumber.trim().length() < 7) {
            throw new InvalidInputException("Contact number must be at least 7 digits.");
        }
        this.contactNumber = contactNumber.trim();
    }

    @Override
    public void displayDetails() {
        System.out.println(getFormattedDetails());
    }

    @Override
    public String toString() {
        return "[" + getRole() + "] ID: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender;
    }
}

