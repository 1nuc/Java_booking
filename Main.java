import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Customer_Login login = new Customer_Login();
        Customer_Register register = new Customer_Register(null, null, null, null, null);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Hall Booking System!");

        Customer customer = new Customer(null, null, null, 0, 0, null, null, null, null, null, null, null);

        // Loop until the user successfully logs in or registers
        /*while (customer == null) {
            System.out.print("Do you want to (1) Register or (2) Login? Enter 1 or 2 (or 3 to exit): ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                if (choice == 1) {
                    customer = register.Set_info();  // Return the customer object upon registration
                } else if (choice == 2) {
                    customer = login.log();  // Return the customer object upon login
                } else if (choice == 3) {
                    System.out.println("Exiting the system...");
                    return; // Exit the program
                } else {
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
                }

                if (customer == null) {
                    System.out.println("Failed to register or login. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }*/

        System.out.println("Login successful!");

        boolean continueBooking = true;
        while (continueBooking) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. View Available Halls");
            System.out.println("2. Book a Hall");
            System.out.println("3. View and Filter Bookings");
            System.out.println("4. Cancel a Booking");
            System.out.println("5. Exit");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character after integer input

                switch (choice) {
                    case 1:
                        customer.view_file("Hall_Availability.txt");
                        break;
                    case 2:
                        //bookHall(scanner, customer);
                        break;
                    case 3:
                        customer.Hall_search_filter("bookings.txt");
                        break;
                    case 4:
                        System.out.print("Enter the Booking ID to cancel: ");
                        String bookingID = scanner.nextLine();
                        customer.cancelBooking(bookingID);
                        break;
                    case 5:
                        continueBooking = false;
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume the invalid input
            }
        }

        scanner.close();
        System.out.println("Thank you for using the Hall Booking System!");
    }
}




/*     private static void bookHall(Scanner scanner, Customer customer) {
        List<Hall> halls = Arrays.asList(
            new Hall("H1", "Auditorium", 1000, 300.0),
            new Hall("H2", "Banquet Hall", 300, 100.0),
            new Hall("H3", "Meeting Room", 30, 50.0)
        );
        viewAvailableHalls(customer);
        System.out.print("Enter the ID of the Hall you want to book: ");
        String hallID = scanner.nextLine();
        Hall selectedHall = halls.stream().filter(h -> h.getHallID().equals(hallID)).findFirst().orElse(null);

        if (selectedHall != null) {
            System.out.print("Enter number of hours for the booking: ");
            int hours = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            customer.bookHall(selectedHall, hours);
        } else {
            System.out.println("Invalid Hall ID.");
        }
    }
        */


    

