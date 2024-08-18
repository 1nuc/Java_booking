package Customer;

import Schedular.availibility_schedule;

import java.io.*;
import java.util.*;
import java.nio.file.*; // Import added for Files and Paths
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class Customer extends availibility_schedule{
    private String firstName;
    private String lastName;
    private String password;
    private String mobileNumber;
    private String email;
    private String address;
    private String customerID;

    // Constructor

    Customer(String Hall_ID, String Hall_Type, String Hall_Location, double cost_per_hour, int Hall_Capacity, 
    String Start_Date, String End_Date, List<String> available_days,String customerID, String firstName, 
    String lastName, String password, String mobileNumber, String email, String address, String M_Start_Date, String M_End_Date, String M_Remarks){
        super(Hall_ID, Hall_Type, Hall_Location, cost_per_hour, Hall_Capacity, Start_Date, End_Date, available_days,  M_Start_Date, M_End_Date, M_Remarks);
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.address = address;
    }
    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerID() {
        return customerID;
    }
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }    

    // Update Profile 
    public void updateProfile(String mobileNumber, String email, String address) {
        setMobileNumber(mobileNumber);
        
        // Validate email before setting it
        boolean validEmail = false;
        while (!validEmail) {
            if (email_checking(email)) {
                setEmail(email);
                validEmail = true;
            } else {
                System.out.println("Invalid email. Please enter a valid email address:");
                try (Scanner scanner = new Scanner(System.in)) {
                    email = scanner.nextLine();
                }
            }
        }
        
        setAddress(address);
        updateCustomerFile();
    }
    
    private void updateCustomerFile() {
        // Update customer.txt with the new information
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/customer.txt")));
            for (int i = 0; i < fileContent.size(); i++) {
                if (fileContent.get(i).startsWith(getCustomerID())) {
                    fileContent.set(i, getCustomerID() + ", " + getFirstName() + ", " + getLastName() + ", " + getEmail() + ", " +
                            "Customer" + ", " + getPassword() + ", " + getAddress() + ", " + getMobileNumber());
                    break;
                }
            }
            Files.write(Paths.get("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/customer.txt"), fileContent);
            System.out.println("Profile updated successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while updating the profile.");
        }
    }
    
    private Boolean email_checking(String email) {
        if (Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email)) {
            return true;
        } else {
            System.out.println("Incorrect Email entered. Please try again.");
            return false;
        }
    }


    @Override
    public void Hall_search_filter(String FileName) {
        super.Hall_search_filter(FileName);
        try (Scanner input = new Scanner(System.in)) {
            Path filePath = Paths.get(FileName);
    
            try {
                List<String> fileLines = Files.readAllLines(filePath);
                String search;
                boolean found = false;
    
                System.out.println("Type or input anything to search for booking information: ");
                search = input.nextLine();
    
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
                System.out.println("\n--- Upcoming Bookings ---");
                for (String line : fileLines) {
                    if (line.contains(search) && line.contains(this.getCustomerID())) {
                        String[] bookingData = line.split(",");
                        LocalDateTime bookingDate = LocalDateTime.parse(bookingData[3], formatter);
                        String status = bookingData[6];
    
                        if (bookingDate.isAfter(now) && !status.equals("Cancelled")) {
                            found = true;
                            System.out.println(line);
                        }
                    }
                }
    
                System.out.println("\n--- Past Bookings ---");
                for (String line : fileLines) {
                    if (line.contains(search) && line.contains(this.getCustomerID())) {
                        String[] bookingData = line.split(",");
                        LocalDateTime bookingDate = LocalDateTime.parse(bookingData[3], formatter);
    
                        if (bookingDate.isBefore(now)) {
                            found = true;
                            System.out.println(line);
                        }
                    }
                }
    
                if (!found) {
                    System.out.println("No bookings found for your search.");
                }
    
            } catch (IOException e) {
                System.out.println("Error reading the bookings file.");
            } catch (DateTimeParseException e) {
                System.out.println("Error: Unable to parse the booking date. Please check the date format in bookings.txt.");
            }
        }
    }

    
}





