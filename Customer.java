import java.io.*;
import java.util.*;
import java.nio.file.*; // Import added for Files and Paths
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Customer extends Hall_Management {
    private String firstName;
    private String lastName;
    private String password;
    private String mobileNumber;
    private String email;
    private String address;
    private String customerID;

    // Constructor

    Customer(String Hall_ID, String Hall_Type, String Hall_Location, double cost_per_hour, int Hall_Capacity,String customerID, String firstName, String lastName, String password, String mobileNumber, String email, String address){
        super(Hall_ID, Hall_Type, Hall_Location, cost_per_hour, Hall_Capacity);
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


    

    // Login Method


    // Update Profile Method
    public void updateProfile(String mobileNumber, String email, String address) {
        setMobileNumber(mobileNumber);
        setEmail(email);
        setAddress(address);
        updateCustomerFile();
    }

    private void updateCustomerFile() {
        // Update customer.txt with the new information
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get("customer.txt")));
            for (int i = 0; i < fileContent.size(); i++) {
                if (fileContent.get(i).contains(getCustomerID())) {
                    fileContent.set(i, getCustomerID() + "," + getFirstName() + "," + getLastName() + "," + getPassword() + "," +
                            getMobileNumber() + "," + getEmail() + "," + getAddress());
                    break;
                }
            }
            Files.write(Paths.get("customer.txt"), fileContent);
            System.out.println("Profile updated successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while updating the profile.");
        }
    }


    // View available halls method (assuming Hall class exists and Scheduler class provides hall data)
    @Override
    public void view_file(String FileName){
        super.view_file(FileName);
    

    }
    
public void bookHall(Hall hall, int hours) {
    try {
        double totalCost = hall.getBookingRate() * hours;

        // Process the payment (assume it's always successful for now)
        processPayment(totalCost);

        // Generate a unique booking ID and get the current date and time
        String bookingID = "BK" + new Random().nextInt(100000, 999999);
        LocalDateTime bookingDate = LocalDateTime.now();

        // Format the bookingDate to include only the date, hour, and minute
        String formattedDateTime = bookingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        // Save the booking to bookings.txt in CSV format
        BufferedWriter writer = new BufferedWriter(new FileWriter("bookings.txt", true));
        writer.write(bookingID + "," + this.customerID + "," + hall.getName() + "," + 
                     formattedDateTime + "," + hours + "," + totalCost + ",Confirmed\n");
        writer.close();

        // Save the receipt to receipts.txt in CSV format
        BufferedWriter receiptWriter = new BufferedWriter(new FileWriter("receipts.txt", true));
        receiptWriter.write(this.customerID + "," + hall.getName() + "," + formattedDateTime + "," + 
                            hours + "," + totalCost + ",Paid\n");
        receiptWriter.close();

        System.out.println("Payment processed for booking: " + bookingID);
        System.out.println("Booking saved successfully!");
    } catch (IOException e) {
        System.out.println("An error occurred while processing the booking.");
    }
}

    public void cancelBooking(String bookingID) {
        List<Booking> bookings = getBookingsFromFile();
        boolean found = false;
    
        System.out.println("=== Your Current Bookings ===");
    
        // Display all bookings associated with the current customer
        for (Booking booking : bookings) {
            if (booking.getCustomerID().equals(this.customerID)) {
                System.out.println(booking);
            }
        }
    
        // Attempt to find and cancel the specific booking
        for (Booking booking : bookings) {
            if (booking.getBookingID().equals(bookingID) && booking.getCustomerID().equals(this.customerID)) {
                if (booking.canBeCancelled()) {
                    booking.cancel();
                    found = true;
                    System.out.println("Booking " + bookingID + " has been cancelled.");
                } else {
                    System.out.println("Booking " + bookingID + " cannot be cancelled (must be at least 3 days before the booking date).");
                }
                break;
            }
        }
    
        if (!found) {
            System.out.println("Booking not found or cannot be cancelled.");
        } else {
            saveBookingsToFile(bookings);
        }
    }
      
    // Helper methods for reading and writing bookings
    private List<Booking> getBookingsFromFile() {
        List<Booking> bookings = new ArrayList<>();
        try {
            File file = new File("bookings.txt");
            Scanner scanner = new Scanner(file);
    
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
            while (scanner.hasNextLine()) {
                String bookingInfo = scanner.nextLine();
                String[] bookingData = bookingInfo.split(",");
    
                if (bookingData.length == 7) {
                    String bookingID = bookingData[0];
                    String customerID = bookingData[1];
                    String hallName = bookingData[2];
                    LocalDateTime bookingDate = LocalDateTime.parse(bookingData[3], formatter);
                    int hours = Integer.parseInt(bookingData[4]);
                    double totalCost = Double.parseDouble(bookingData[5]);
                    String status = bookingData[6];
    
                    Booking booking = new Booking(bookingID, customerID, hallName, bookingDate, hours, totalCost, status);
                    bookings.add(booking);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: bookings.txt file not found.");
        } catch (DateTimeParseException e) {
            System.out.println("Error: Unable to parse the booking date. Please check the date format in bookings.txt.");
        }
        return bookings;
    }
    
    private void saveBookingsToFile(List<Booking> bookings) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bookings.txt"))) {
            for (Booking booking : bookings) {
                writer.write(booking.toString());
                writer.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to bookings.txt file.");
        }
    }

    public void processPayment(double amount) {
        System.out.println("Processing payment of RM " + amount);
        // Here you could integrate with a payment gateway if needed
        System.out.println("Payment processed successfully!");
    }

    public void viewBookings() {
        try {
            File file = new File("bookings.txt");
            Scanner scanner = new Scanner(file);
            System.out.println("=== Your Bookings ===");
            boolean hasBookings = false;

            while (scanner.hasNextLine()) {
                String bookingInfo = scanner.nextLine();
                if (bookingInfo.contains("Customer ID: " + this.customerID)) {
                    hasBookings = true;
                    System.out.println(bookingInfo);
                }
            }

            if (!hasBookings) {
                System.out.println("No bookings found for your account.");
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: bookings.txt file not found.");
        }
    }

    @Override
    public void Hall_search_filter(String FileName){

        
        try (Scanner input = new Scanner(System.in)) {
            Path filePath = Paths.get(FileName);
   
            try {
                List<String> fileLines = Files.readAllLines(filePath);
                String search;
                boolean found = false;
   
                System.out.println("Type or input anything to search for booking information: ");
                search = input.nextLine();
   
                LocalDateTime now = LocalDateTime.now();
   
                // Correct date formatter matching the format in the bookings.txt file
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
   
                System.out.println("\n--- Upcoming Bookings ---");
                for (String line : fileLines) {
                    if (line.contains(search)) {
                        String[] bookingData = line.split(",");
                        LocalDateTime bookingDate = LocalDateTime.parse(bookingData[3], formatter); // Use formatter here
                        String status = bookingData[6]; // Assuming status is at index 6
   
                        if (bookingDate.isAfter(now) && !status.equals("Cancelled")) {
                            found = true;
                            System.out.println(line);
                        }
                    }
                }
   
                System.out.println("\n--- Past Bookings ---");
                for (String line : fileLines) {
                    if (line.contains(search)) {
                        String[] bookingData = line.split(",");
                        LocalDateTime bookingDate = LocalDateTime.parse(bookingData[3], formatter); // Use formatter here
   
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


