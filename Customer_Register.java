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
            System.out.println("First Name:");
            setFirstName(scan.nextLine());
            
            System.out.println("Last Name:");
            setLastName(scan.nextLine());
            
            System.out.println("Address:");
            setAddress(scan.nextLine());
            
            System.out.println("Phone Number:");
            setPhoneNumber(scan.nextLine());
            
            Boolean Valid_Email = false;
            while (!Valid_Email) {
                System.out.println("Email:");
                set_Email(scan.nextLine());
                Valid_Email = email_checking();
            }

            // Generate and set a unique customer ID
            set_ID(generateUniqueCustomerID());
            set_Role("Customer");

            System.out.println("Password:");
            set_Password(scan.nextLine());
        }

        SaveToFile();
        System.out.println("Successfully registered with Customer ID: " + get_ID());
    }

    private String generateUniqueCustomerID() {
        String id;
        do {
            id = "CU" + String.format("%06d", (int) (Math.random() * 1000000));
        } while (idExists(id));
        return id;
    }

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

    private Boolean email_checking() {
        if (Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", get_Email())) {
            return true;
        } else {
            System.out.println("Incorrect Email entered. Please try again.");
            return false;
        }
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
