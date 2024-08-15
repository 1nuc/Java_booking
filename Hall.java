public class Hall {
    private String hallID;
    private String name;
    private int capacity;
    private double bookingRate;

    public Hall(String hallID, String name, int capacity, double bookingRate) {
        this.hallID = hallID;
        this.name = name;
        this.capacity = capacity;
        this.bookingRate = bookingRate;
    }

    public String getHallID() {
        return hallID;
    }

    public String getName() {
        return name;
    }


    public int getCapacity() {
        return capacity;
    }

    public double getBookingRate() {
        return bookingRate;
    }

    @Override
    public String toString() {
        return "Hall [ID=" + hallID + ", Name=" + name + ", Capacity=" + capacity +
                 ", Booking Rate=RM " + bookingRate + " per hour]";
    }
}
