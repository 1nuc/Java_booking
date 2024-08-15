import java.time.*;
import java.time.format.DateTimeFormatter;

public class Booking {
    private String bookingID;
    private String customerID;
    private String hallName;
    private LocalDateTime bookingDate;
    private int hours;
    private double totalCost;
    private String status;

    public Booking(String bookingID, String customerID, String hallName, 
                   LocalDateTime bookingDate, int hours, double totalCost, String status) {
        this.bookingID = bookingID;
        this.customerID = customerID;
        this.hallName = hallName;
        this.bookingDate = bookingDate;
        this.hours = hours;
        this.totalCost = totalCost;
        this.status = status;
    }

    // Getters
    public String getBookingID() { return bookingID; }
    public String getCustomerID() { return customerID; }
    public String getHallName() { return hallName; }
    public LocalDateTime getBookingDate() { return bookingDate; }
    public int getHours() { return hours; }
    public double getTotalCost() { return totalCost; }
    public String getStatus() { return status; }

    // Method to check if booking can be cancelled
    public boolean canBeCancelled() {
        LocalDateTime now = LocalDateTime.now();
        return bookingDate.minusDays(3).isAfter(now);
    }

    // Method to cancel booking
    public void cancel() {
        this.status = "Cancelled";
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return bookingID + "," + customerID + "," + hallName + "," + 
               bookingDate.format(formatter) + "," + hours + "," + totalCost + "," + status;
    }
}

