package Customer;
import Schedular.Hall_Management;
import Schedular.Schedular;
import Schedular.availibility_schedule;

import java.io.*;
import java.util.*;
import java.nio.file.*; // Import added for Files and Paths
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import java.time.LocalTime;

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

    public List<Hall> getAvailableHalls(LocalDateTime bookingDateTime, int hours) {
        List<Hall> allHalls = readAllHalls();
        Set<String> bookedHallIds = getBookedHallIds(bookingDateTime, hours);
        List<Hall> availableHalls = new ArrayList<>();

        for (Hall hall : allHalls) {
            if (!bookedHallIds.contains(hall.getHallID())) {
                availableHalls.add(hall);
            }
        }

        return availableHalls;
    }

    private List<Hall> readAllHalls() {
        List<Hall> halls = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Schedular/Schedular/Hall_Info.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 5) {
                    String hallId = parts[0].trim();
                    String name = parts[1].trim();
                    int capacity = Integer.parseInt(parts[3].trim());
                    double rate = Double.parseDouble(parts[4].trim());
                    halls.add(new Hall(hallId, name, capacity, rate));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Hall_Info.txt: " + e.getMessage());
        }
        return halls;
    }

    private Set<String> getBookedHallIds(LocalDateTime bookingDateTime, int hours) {
        Set<String> bookedHallIds = new HashSet<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/bookings.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String hallName = parts[2].trim();
                    LocalDateTime bookedDateTime = LocalDateTime.parse(parts[3].trim(), formatter);
                    int bookedHours = Integer.parseInt(parts[4].trim());
                    
                    if (isOverlapping(bookedDateTime, bookedHours, bookingDateTime, hours)) {
                        bookedHallIds.add(hallName);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading bookings.txt: " + e.getMessage());
        }
        return bookedHallIds;
    }

    private boolean isOverlapping(LocalDateTime bookedStart, int bookedHours, 
                                  LocalDateTime requestedStart, int requestedHours) {
        LocalDateTime bookedEnd = bookedStart.plusHours(bookedHours);
        LocalDateTime requestedEnd = requestedStart.plusHours(requestedHours);
        return !requestedStart.isAfter(bookedEnd) && !bookedStart.isAfter(requestedEnd);
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

    public void Book_Hall() {
        Hall_Management h = new Hall_Management(email, customerID, address, getcost_per_hour(), getHall_Capacity());
        h.view_file("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Schedular/Schedular/Hall_Info.txt");
    
        // Call Specific_Hall_availability to display available halls and select a hall
        String hallID = Specific_Hall_availability(); 
    
        // Load available halls
        List<String> HallIDs = ID_Of_Hall();
        int index = HallIDs.indexOf(hallID);
    
        if (index != -1) {
            // Get the hall's available days
            List<String> Ava_days = get_available_days();
    
            // Check if the hall is available on the desired date
            try (Scanner input = new Scanner(System.in)) {
                System.out.println("Enter booking start date and time (yyyy-MM-dd hh:mm AM/PM): ");
                String bookingStart = input.nextLine();
                System.out.println("Enter booking end date and time (yyyy-MM-dd hh:mm AM/PM): ");
                String bookingEnd = input.nextLine();
    
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
                LocalDateTime bookingStartTime = LocalDateTime.parse(bookingStart, formatter);
                LocalDateTime bookingEndTime = LocalDateTime.parse(bookingEnd, formatter);
    
                // Check if the booking period is within the hall's available range
                LocalDateTime hallStartDate = LocalDateTime.parse(get_Start_Date(), formatter);
                LocalDateTime hallEndDate = LocalDateTime.parse(get_End_Date(), formatter);
    
                if (!bookingStartTime.isBefore(hallStartDate) && !bookingEndTime.isAfter(hallEndDate)) {
                    // If the booking period is within the available range, proceed to book
                    List<LocalDateTime[]> MaintenancePeriods = read_M_Date_Time(hallID);
                    List<LocalDateTime[]> BookedPeriods = read_Booked_Date_Time(hallID);
    
                    boolean canBook = true;
    
                    // Check if the booking period conflicts with any maintenance or booked periods
                    for (LocalDateTime[] period : MaintenancePeriods) {
                        if (bookingStartTime.isBefore(period[1]) && bookingEndTime.isAfter(period[0])) {
                            canBook = false;
                            break;
                        }
                    }
    
                    if (canBook) {
                        for (LocalDateTime[] period : BookedPeriods) {
                            if (bookingStartTime.isBefore(period[1]) && bookingEndTime.isAfter(period[0])) {
                                canBook = false;
                                break;
                            }
                        }
                    }
    
                    if (canBook) {
                        // Save the booking information to a file
                        saveBooking(hallID, bookingStartTime, bookingEndTime);
                        System.out.println("Hall booked successfully!");
                    } else {
                        System.out.println("The hall is not available during the requested period. Please choose a different time.");
                    }
                } else {
                    System.out.println("The booking period must be within the hall's available range.");
                }
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        } else {
            System.out.println("Hall ID not found.");
        }
    }
    
    
    private void saveBooking(String hallID, LocalDateTime bookingStart, LocalDateTime bookingEnd) {
        try (PrintWriter write = new PrintWriter(new FileWriter("src/Customer/Customer/Hall_Bookings.txt", true))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            write.println("Hall ID: " + hallID + ", Booking Start: " + bookingStart.format(formatter) + ", Booking End: " + bookingEnd.format(formatter));
            System.out.println("Booking saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving the booking.");
            e.printStackTrace();
        }
    }

    private List<LocalDateTime[]> read_Booked_Date_Time(String HallID) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        List<LocalDateTime[]> bookedPeriods = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Schedular/Schedular/Hall_Bookings.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line based on the format you've used to store bookings
                String[] parts = line.split(", ");
                if (parts[1].equals(HallID)) {
                    try {
                        LocalDateTime bookingStart = LocalDateTime.parse(parts[2].substring("Booking Start: ".length()), formatter);
                        LocalDateTime bookingEnd = LocalDateTime.parse(parts[3].substring("Booking End: ".length()), formatter);
                        bookedPeriods.add(new LocalDateTime[]{bookingStart, bookingEnd});
                    } catch (DateTimeParseException e) {
                        System.out.println("Error parsing the booking date in line: " + line);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the booking periods.");
            e.printStackTrace();
        }
    
        return bookedPeriods;
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
                    saveCancelledBooking(booking);
                    updateReceiptForCancellation(booking);
                    bookings.remove(booking);
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
    
    private void updateReceiptForCancellation(Booking booking) {
        String receiptFilePath = "C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/receipts.txt";
        List<String> updatedReceipts = new ArrayList<>();
        boolean receiptFound = false;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(receiptFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                
                // Assuming the booking ID is the last element in the parts array
                String bookingID = parts[parts.length - 1];
    
                if (bookingID.equals(booking.getBookingID())) {
                    // Receipt matches the booking ID, so we delete it by skipping this line
                    receiptFound = true;
                } else {
                    updatedReceipts.add(line); // Keep the receipt as it is
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading receipts file: " + e.getMessage());
            return;
        }
    
        if (!receiptFound) {
            System.out.println("Receipt not found for the cancelled booking.");
            return;
        }
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(receiptFilePath))) {
            for (String receipt : updatedReceipts) {
                writer.write(receipt);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating receipts file: " + e.getMessage());
        }
    }
    
    private void saveCancelledBooking(Booking booking) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/cbookings.txt", true))) {
            writer.write(booking.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to cbookings.txt file.");
        }
    }

    // Helper methods for reading and writing bookings
    private List<Booking> getBookingsFromFile() {
        List<Booking> bookings = new ArrayList<>();
        try {
            File file = new File("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/bookings.txt");
            Scanner scanner = new Scanner(file);
    
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
            while (scanner.hasNextLine()) {
                String bookingInfo = scanner.nextLine();
                String[] bookingData = bookingInfo.split(",");
    
                if (bookingData.length == 8) {  // Changed from 7 to 8
                    String bookingID = bookingData[0];
                    String customerID = bookingData[1];
                    String hallID = bookingData[2];
                    String hallName = bookingData[3];
                    LocalDateTime bookingDate = LocalDateTime.parse(bookingData[4], formatter);
                    int hours = Integer.parseInt(bookingData[5]);
                    double totalCost = Double.parseDouble(bookingData[6]);
                    String status = bookingData[7];
                
                    Booking booking = new Booking(bookingID, customerID, hallID, hallName, bookingDate, hours, totalCost, status);
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/bookings.txt"))) {
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
        File bookingsFile = new File("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/bookings.txt");
        File cancelledBookingsFile = new File("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/cbookings.txt");
        File receiptsFile = new File("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/receipts.txt");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        System.out.println("=== Your Bookings ===");
        System.out.println("\n--- Upcoming Bookings ---");
        printBookings(bookingsFile, true, now, formatter);

        System.out.println("\n--- Past Bookings ---");
        printBookings(bookingsFile, false, now, formatter);

        System.out.println("\n--- Cancelled Bookings ---");
        printCancelledBookings(cancelledBookingsFile, now, formatter);

        System.out.println("\n--- Receipts ---");
        printReceipts(receiptsFile);

    } catch (FileNotFoundException e) {
        System.out.println("Error: One or more required files not found.");
    }
}

private void printBookings(File file, boolean upcoming, LocalDateTime now, DateTimeFormatter formatter) throws FileNotFoundException {
    Scanner scanner = new Scanner(file);
    boolean hasBookings = false;

    while (scanner.hasNextLine()) {
        String bookingInfo = scanner.nextLine();
        String[] bookingData = bookingInfo.split(",");
        if (bookingData.length > 4 && bookingData[1].equals(this.customerID)) {
            LocalDateTime bookingDate = LocalDateTime.parse(bookingData[4], formatter);
            if ((upcoming && bookingDate.isAfter(now)) || (!upcoming && bookingDate.isBefore(now))) {
                System.out.println(bookingInfo);
                hasBookings = true;
            }
        }
    }

    if (!hasBookings) {
        System.out.println("No " + (upcoming ? "upcoming" : "past") + " bookings found.");
    }

    scanner.close();
}

private void printCancelledBookings(File file, LocalDateTime now, DateTimeFormatter formatter) throws FileNotFoundException {
    Scanner scanner = new Scanner(file);
    boolean hasBookings = false;

    while (scanner.hasNextLine()) {
        String bookingInfo = scanner.nextLine();
        String[] bookingData = bookingInfo.split(",");
        if (bookingData.length > 4 && bookingData[1].equals(this.customerID)) {
            LocalDateTime bookingDate = LocalDateTime.parse(bookingData[4], formatter);
            System.out.println(bookingInfo + (bookingDate.isAfter(now) ? " (Upcoming)" : " (Past)"));
            hasBookings = true;
        }
    }

    if (!hasBookings) {
        System.out.println("No cancelled bookings found.");
    }

    scanner.close();
}

private void printReceipts(File file) throws FileNotFoundException {
    Scanner scanner = new Scanner(file);
    boolean hasReceipts = false;

    while (scanner.hasNextLine()) {
        String receiptInfo = scanner.nextLine();
        String[] receiptData = receiptInfo.split(",");
        if (receiptData.length > 0 && receiptData[0].equals(this.customerID)) {
            System.out.println(receiptInfo);
            hasReceipts = true;
        }
    }

    if (!hasReceipts) {
        System.out.println("No receipts found.");
    }

    scanner.close();
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



