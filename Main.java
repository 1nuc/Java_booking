package Customer;
import java.util.Random;
import java.time.LocalDateTime;


        // Customer registration
        // Customer_Register registration = new Customer_Register(null, null, null, null, null, null, null, null);
        // registration.Set_info();

        // Customer login
        // Customer_Login login = new Customer_Login();
        // login.log();

        public class Main {
            public static void main(String[] args) {
                // Create a customer object
                Customer customer = new Customer(
                    null, 
                    null, 
                    null, 
                    0, 
                    0, 
                    null, 
                    null, 
                    null, 
                    "CU123456",
                    "g7bah", 
                    "g7bah", 
                    "1", 
                    null, 
                    "g7bah@gmail.com",
                    null, 
                    null, 
                    null, 
                    null
                );

        // View available halls
        // customer.view_file("halls.txt"); noooote

        // Create a sample hall for booking
        // Hall sampleHall = new Hall("H001", "Grand Hall", 500, 150.0);

        // Book a hall
        customer.Book_Hall(); 

        // View bookings
        // customer.viewBookings();

        // Search and filter bookings
        // customer.Hall_search_filter("C://Users//sagl3//OneDrive//Desktop//Java_Assignment//Java_Assignment//src//Customer//Customer//bookings.txt");

        // Update profile
        // customer.updateProfile("9876543210", "new", "456 Elm St");

        // // Generate a random booking ID for cancellation
        // String randomBookingID = "BK" + new Random().nextInt(100000, 999999);

        // // Cancel a booking
        // customer.cancelBooking("BK185394");

        // System.out.println("All operations completed.");
    }
}
