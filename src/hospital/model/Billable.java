package hospital.model;

/**
 * Interface demonstrating Abstraction for entities that incur costs or generate invoices.
 */
public interface Billable {
    double calculateTotalBill();
    String generateInvoice();
}

