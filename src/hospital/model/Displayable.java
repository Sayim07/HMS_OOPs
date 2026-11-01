package hospital.model;

/**
 * Interface demonstrating Abstraction.
 * Any entity implementing this interface must define how its details are formatted and displayed.
 */
public interface Displayable {
    void displayDetails();
    String getFormattedDetails();
}

