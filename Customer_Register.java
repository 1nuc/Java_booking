package Customer;
import Adminstrator.admins;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

class Customer_Register extends admins {
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;

    // Constructor
    public Customer_Register(String firstName, String lastName, String Email, String ID, String Role, String Password, String address, String phoneNumber) {
        super(firstName + " " + lastName, Email, ID, Role, Password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters for new fields
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public void Set_info() {
        try (Scanner scan = new Scanner(System.in)) {
            // First Name validation
            while (true) {
                System.out.println("First Name (letters only):");
                String input = scan.nextLine().trim();
                if (input.matches("[a-zA-Z]+")) {
                    setFirstName(input);
                    break;
                } else {
                    System.out.println("Invalid input. Please enter letters only.");
                }
            }
            
            // Last Name validation
            while (true) {
                System.out.println("Last Name (letters only):");
                String input = scan.nextLine().trim();
                if (input.matches("[a-zA-Z]+")) {
                    setLastName(input);
                    break;
                } else {
                    System.out.println("Invalid input. Please enter letters only.");
                }
            }
            
            // Address validation (basic, non-empty)
            while (true) {
                System.out.println("Address:");
                String input = scan.nextLine().trim();
                if (!input.isEmpty()) {
                    setAddress(input);
                    break;
                } else {
                    System.out.println("Address cannot be empty. Please try again.");
                }
            }
            
            // Phone Number validation (basic, digits only)
            while (true) {
                System.out.println("Phone Number (digits only):");
                String input = scan.nextLine().trim();
                if (input.matches("\\d+")) {
                    setPhoneNumber(input);
                    break;
                } else {
                    System.out.println("Invalid phone number. Please enter digits only.");
                }
            }
            
            // Email validation
            while (true) {
                System.out.println("Email:");
                String input = scan.nextLine().trim();
                if (email_checking(input)) {
                    set_Email(input);
                    break;
                } else {
                    System.out.println("Invalid email format. Please try again.");
                }
            }

            // Generate and set a unique customer ID
            set_ID(generateUniqueCustomerID());
            set_Role("Customer");

            // Password validation (basic, non-empty)
            while (true) {
                System.out.println("Password:");
                String input = scan.nextLine().trim();
                if (!input.isEmpty()) {
                    set_Password(input);
                    break;
                } else {
                    System.out.println("Password cannot be empty. Please try again.");
                }
            }
        }

        SaveToFile();
        System.out.println("Successfully registered with Customer ID: " + get_ID());
    }

    
    private Boolean email_checking(String email) {
    return Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email);
    }

    private String generateUniqueCustomerID() {
        String id;
        do {
            id = "CU" + String.format("%06d", (int) (Math.random() * 1000000));
        } while (idExists(id));
        return id;
    }

    // checks if the customerid already exists
    private boolean idExists(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/customer.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length > 0 && parts[0].equals(id)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking for existing IDs: " + e.getMessage());
        }
        return false;
    }

    public void SaveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/customer.txt", true))) {
            writer.write(get_ID() + ", " + getFirstName() + ", " + getLastName() + ", " + get_Email() + ", " + get_Role() + ", " + get_Password() + ", " + getAddress() + ", " + getPhoneNumber());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("An error occurred writing to the file: " + e.getMessage());
        }
    }
}
