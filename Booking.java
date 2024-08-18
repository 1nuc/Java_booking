package Customer;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.util.*;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import Schedular.availibility_schedule;

public class Booking extends availibility_schedule{
    private String bookingID;
    private String customerID;
    private String hallID;
    private String hallName;
    private LocalDateTime bookingDate;
    private int hours;
    private double totalCost;
    private String status;

    Booking(String Hall_ID, String Hall_Type, String Hall_Location, double cost_per_hour, int Hall_Capacity, 
    String Start_Date, String End_Date, List<String> available_days,String customerID, String M_Start_Date, String M_End_Date, String M_Remarks){
        super(Hall_ID, Hall_Type, Hall_Location, cost_per_hour, Hall_Capacity, Start_Date, End_Date, available_days,  M_Start_Date, M_End_Date, M_Remarks);
        this.customerID = customerID;
    }

    // Getters
    public String getBookingID() { return bookingID; }
    public String getCustomerID() { return customerID; }
    public String getHallName() { return hallName; }
    public LocalDateTime getBookingDate() { return bookingDate; }
    public int getHours() { return hours; }
    public double getTotalCost() { return totalCost; }
    public String getStatus() { return status; }
    public String getHallID() { return hallID; }

    // Method to check if booking can be cancelled
    public boolean canBeCancelled() {
        LocalDateTime now = LocalDateTime.now();
        return bookingDate.minusDays(3).isAfter(now);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("%s,%s,%s,%s,%s,%d,%.2f,%s",
            bookingID, customerID, hallID, hallName, bookingDate.format(formatter), hours, totalCost, status);
    }

    // cancel 

public void cancelBooking() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    List<String> bookings = new ArrayList<>();
    boolean found = false;

    System.out.println("=== Your Current Bookings ===");

    try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/bookings.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(", ");
            String bookingCustomerID = parts[1].split(": ")[1];
            
            if (bookingCustomerID.equals(this.customerID)) {
                System.out.println(line);
                bookings.add(line);
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading booking file: " + e.getMessage());
        return;
    }

    if (bookings.isEmpty()) {
        System.out.println("You have no current bookings.");
        return;
    }

    try (Scanner scan = new Scanner(System.in)) {
        System.out.println("Enter the Booking ID of the booking you want to cancel: ");
        String bookingIDToCancel = scan.nextLine();

        for (String booking : bookings) {
            String[] parts = booking.split(", ");
            String bookingID = parts[0].split(": ")[1];
            String bookingStartDate = parts[4].split(": ")[1];

            if (bookingID.equals(bookingIDToCancel)) {
                LocalDateTime bookingDate = LocalDateTime.parse(bookingStartDate, formatter);
                if (canBeCancelled(bookingDate)) {
                    found = true;
                    saveCancelledBooking(booking);
                    removeBookingFromFile(booking);
                    updateReceiptForCancellation(bookingID);
                    System.out.println("Booking with ID " + bookingIDToCancel + " has been cancelled.");
                } else {
                    System.out.println("Booking with ID " + bookingIDToCancel + " cannot be cancelled (must be at least 3 days before the booking date).");
                }
                break;
            }
        }

        if (!found) {
            System.out.println("Booking not found or cannot be cancelled.");
        }
    }
}

private boolean canBeCancelled(LocalDateTime bookingDate) {
    LocalDateTime now = LocalDateTime.now();
    return bookingDate.minusDays(3).isAfter(now);
}

private void saveCancelledBooking(String booking) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/cbookings.txt", true))) {
        writer.write(booking + "\n");
    } catch (IOException e) {
        System.out.println("Error writing to cbookings.txt file: " + e.getMessage());
    }
}

private void removeBookingFromFile(String bookingToRemove) {
    List<String> updatedBookings = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/bookings.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.equals(bookingToRemove)) {
                updatedBookings.add(line);
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading booking file: " + e.getMessage());
        return;
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/bookings.txt"))) {
        for (String booking : updatedBookings) {
            writer.write(booking);
            writer.newLine();
        }
    } catch (IOException e) {
        System.out.println("Error updating booking file: " + e.getMessage());
    }
}

private void updateReceiptForCancellation(String bookingID) {
    String receiptFilePath = "C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/receipts.txt";
    List<String> updatedReceipts = new ArrayList<>();
    boolean receiptFound = false;

    try (BufferedReader reader = new BufferedReader(new FileReader(receiptFilePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(", ");
            
            // Assuming the booking ID is the last element in the parts array
            String receiptBookingID = parts[parts.length - 1].split(": ")[1];

            if (receiptBookingID.equals(bookingID)) {
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
        System.out.println("Receipt updated for the cancelled booking.");
    } catch (IOException e) {
        System.out.println("Error updating receipts file: " + e.getMessage());
    }
}


    // book hall

public void viewFile(String fileName) {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    } catch (IOException e) {
        System.out.println("Error reading the file: " + e.getMessage());
    }
}

public void bookHall() {
    Extract_date_Time("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Schedular/Schedular/Hall_Availibility.txt"
);
    DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd hh a");
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");

    viewFile("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Schedular/Schedular/Hall_Info.txt"
);

    List<String>Hall_IDs=ID_Of_Hall();
    List<String>HallTypes=Type_Of_Hall();

    try (Scanner scan = new Scanner(System.in)) {
        System.out.println("Enter the Hall ID to book: ");
        String hallID = scan.nextLine();

        Specific_Hall_availability(hallID);
        
        int ix=Hall_IDs.indexOf(hallID);
        String hallType =HallTypes.get(ix);
        

        LocalDateTime bookingStartDateTime = null;
        LocalDateTime bookingEndDateTime = null;

        // Enter Booking Start Date and Time
        while (bookingStartDateTime == null) {
            System.out.println("Enter the booking start date (yyyy-MM-dd): ");
            String bookingStartDate = scan.nextLine();
            System.out.println("Enter the booking start time (hh:mm AM/PM): ");
            String bookingStartTime = scan.nextLine();

            if (!bookingStartDate.matches("\\d{4}-\\d{2}-\\d{2}") || !Year_Valid(bookingStartDate)) {
                System.err.println("Invalid date format");
            } else {
                try {
                    LocalDate parsedDate = LocalDate.parse(bookingStartDate, dateFormat);
                    LocalTime parsedTime = LocalTime.parse(bookingStartTime, timeFormat);
                    bookingStartDateTime = LocalDateTime.of(parsedDate, parsedTime);
                } catch (DateTimeParseException e) {
                    System.out.println("Incorrect date format entered");
                }
            }
        }

        // Enter Booking End Date and Time
        while (bookingEndDateTime == null) {
            System.out.println("Enter the booking end date (yyyy-MM-dd): ");
            String bookingEndDate = scan.nextLine();
            System.out.println("Enter the booking end time (hh:mm AM/PM): ");
            String bookingEndTime = scan.nextLine();

            if (!bookingEndDate.matches("\\d{4}-\\d{2}-\\d{2}") || !Year_Valid(bookingEndDate)) {
                System.err.println("Invalid date format");
            } else {
                try {
                    LocalDate parsedDate = LocalDate.parse(bookingEndDate, dateFormat);
                    LocalTime parsedTime = LocalTime.parse(bookingEndTime, timeFormat);
                    bookingEndDateTime = LocalDateTime.of(parsedDate, parsedTime);
                } catch (DateTimeParseException e) {
                    System.out.println("Incorrect date format entered");
                }
            }
        }


        LocalDateTime startDate = LocalDateTime.parse(get_Start_Date(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(get_End_Date(), formatter);
        if (bookingStartDateTime.isBefore(startDate) || bookingEndDateTime.isAfter(endDate)) {
            System.out.println("booking date and time must be within the availability range.");
            Schedule_Maintenance();
        } else if (!bookingStartDateTime.isBefore(bookingEndDateTime)) {
            System.out.println("booking date and time start time must be before the end time.");
            Schedule_Maintenance();
        }else{
            
            List<LocalDateTime[]> bookings = readBookingDateTime(hallID);
            List<LocalDateTime[]> maintenance = read_M_Date_Time(hallID);
            List<String> availableDays = get_available_days();
    
            String dayName = bookingStartDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    
            boolean isAvailable = true;
            for (LocalDateTime[] period : bookings) {
                if (bookingStartDateTime.isBefore(period[1]) && bookingEndDateTime.isAfter(period[0])) {
                    isAvailable = false;
                    break;
                }
            }
            for (LocalDateTime[] period : maintenance) {
                if (bookingStartDateTime.isBefore(period[1]) && bookingEndDateTime.isAfter(period[0])) {
                    isAvailable = false;
                    break;
                }
            }
    
    
            if (!availableDays.contains(dayName)) {
                isAvailable = false;
            }
    
            if (isAvailable) {
                System.out.println("Enter remarks for booking:");
                String remarks = scan.nextLine();
                double amount = calculatePayment(hallType, bookingStartDateTime, bookingEndDateTime);
                String bookingID = generateBookingID();
                processPayment(bookingID, hallID, hallType, bookingStartDateTime, bookingEndDateTime, remarks, amount);
            } else {
                System.out.println("Hall not available. Please select a different date or time.");
                bookHall(); // Recur to ask for new dates
            }

        }

        
    } catch (Exception e) {
        System.out.println("Error occurred during booking: " + e.getMessage());
    }
}

protected List<LocalDateTime[]> readBookingDateTime(String hallID) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh a");
    List<LocalDateTime[]> bookingPeriods = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/bookings.txt"
))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(": |, ");
            if (parts[1].equals(hallID)) {
                try {
                    LocalDateTime start = LocalDateTime.parse(parts[3], formatter);
                    LocalDateTime end = LocalDateTime.parse(parts[5], formatter);
                    bookingPeriods.add(new LocalDateTime[]{start, end});
                } catch (Exception e) {
                    System.out.println("Error parsing booking date: " + Arrays.toString(parts));
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading the booking file: " + e.getMessage());
    }

    return bookingPeriods;
}

public void saveBooking(String hallID, String HallType, LocalDateTime startDate, LocalDateTime endDate, String remarks) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h a");
    String bookingDetails = String.format("HallID: %s, Hall Type: %s,  Start date: %s, End date: %s, Remarks: %s",
            hallID,HallType, startDate.format(formatter), endDate.format(formatter), remarks);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/bookings.txt"
, true))) {
        writer.write(bookingDetails);
        writer.newLine();
        System.out.println("Hall with ID: "+hallID+" Type: "+ HallType+ "\nis reserved from "+startDate.format(formatter)+" to "+endDate.format(formatter)+" for " + remarks);
    } catch (IOException e) {
        System.out.println("Error saving booking: " + e.getMessage());
    }
}

public void processPayment(String hallID, String HallType, LocalDateTime startDate, LocalDateTime endDate, String remarks, double amount) {
    System.out.println("Processing payment of RM " + amount);
    // Here you could integrate with a payment gateway if needed
    System.out.println("Payment processed successfully!");

    // Save the booking after successful payment
    saveBooking(hallID, HallType, startDate, endDate, remarks);
}

public double calculatePayment(String hallType, LocalDateTime startDate, LocalDateTime endDate) {
    double costPerHour;
    
    // Determine the cost per hour based on the hall type
    switch (hallType) {
        case "Auditorium":
            costPerHour = 300.0;
            break;
        case "Banquet_Hall":
            costPerHour = 100.0;
            break;
        case "Meeting_Room":
            costPerHour = 50.0;
            break;
        default:
            costPerHour = 0.0; // Handle unrecognized hall type
            System.out.println("Unrecognized hall type: " + hallType);
            return 0.0;
    }

    // Calculate the duration in hours
    long hours = java.time.Duration.between(startDate, endDate).toHours();

    if (hours <= 0) {
        hours = 1; // Minimum charge for 1 hour
    }

    // Return the total cost
    return hours * costPerHour;
}
   



    // view booking with cancelled bookings and receipts
    
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
            String[] bookingData = bookingInfo.split(", ");
            if (bookingData.length > 5 && bookingData[1].split(": ")[1].equals(this.customerID)) {
                LocalDateTime bookingDate = LocalDateTime.parse(bookingData[4].split(": ")[1], formatter);
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
            String[] bookingData = bookingInfo.split(", ");
            if (bookingData.length > 5 && bookingData[1].split(": ")[1].equals(this.customerID)) {
                LocalDateTime bookingDate = LocalDateTime.parse(bookingData[4].split(": ")[1], formatter);
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
            String[] receiptData = receiptInfo.split(", ");
            if (receiptData.length > 1 && receiptData[1].split(": ")[1].equals(this.customerID)) {
                System.out.println(receiptInfo);
                hasReceipts = true;
            }
        }
    
        if (!hasReceipts) {
            System.out.println("No receipts found.");
        }
    
        scanner.close();
    }



    // save bookings

private String generateBookingID() {
    String prefix = "BK";
    String uniqueID;
    do {
        uniqueID = prefix + String.format("%06d", new Random().nextInt(1000000));
    } while (isBookingIDExists(uniqueID));
    return uniqueID;
}

private boolean isBookingIDExists(String bookingID) {
    try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/bookings.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(bookingID)) {
                return true;
            }
        }
    } catch (IOException e) {
        System.out.println("Error checking booking ID: " + e.getMessage());
    }
    return false;
}

private String generateReceiptID() {
    String prefix = "INV";
    String uniqueID;
    do {
        uniqueID = prefix + String.format("%06d", new Random().nextInt(1000000));
    } while (isReceiptIDExists(uniqueID));
    return uniqueID;
}

private boolean isReceiptIDExists(String receiptID) {
    try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/receipts.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(receiptID)) {
                return true;
            }
        }
    } catch (IOException e) {
        System.out.println("Error checking receipt ID: " + e.getMessage());
    }
    return false;
}

public void processPayment(String bookingID, String hallID, String hallType, LocalDateTime startDate, LocalDateTime endDate, String remarks, double amount) {
    System.out.println("Processing payment of RM " + amount);
    // Here you could integrate with a payment gateway if needed
    System.out.println("Payment processed successfully!");

    // Save the booking after successful payment
    saveBooking(bookingID, hallID, hallType, startDate, endDate, remarks);
    saveReceipt(bookingID, hallID, hallType, startDate, endDate, amount);
}

public void saveBooking(String bookingID, String hallID, String hallType, LocalDateTime startDate, LocalDateTime endDate, String remarks) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String bookingDetails = String.format("BookingID: %s, CustomerID: %s, HallID: %s, Hall Type: %s, Start date: %s, End date: %s, Remarks: %s",
            bookingID, this.customerID, hallID, hallType, startDate.format(formatter), endDate.format(formatter), remarks);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/bookings.txt", true))) {
        writer.write(bookingDetails);
        writer.newLine();
        System.out.println("Booking saved successfully. Booking ID: " + bookingID);
    } catch (IOException e) {
        System.out.println("Error saving booking: " + e.getMessage());
    }
}

public void saveReceipt(String bookingID, String hallID, String hallType, LocalDateTime startDate, LocalDateTime endDate, double amount) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String receiptID = generateReceiptID();
    String receiptDetails = String.format("ReceiptID: %s, CustomerID: %s, HallID: %s, Hall Type: %s, Start date: %s, End date: %s, Amount: %.2f, BookingID: %s",
            receiptID, this.customerID, hallID, hallType, startDate.format(formatter), endDate.format(formatter), amount, bookingID);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/receipts.txt", true))) {
        writer.write(receiptDetails);
        writer.newLine();
        System.out.println("Receipt saved successfully. Receipt ID: " + receiptID);
    } catch (IOException e) {
        System.out.println("Error saving receipt: " + e.getMessage());
    }
}


}
