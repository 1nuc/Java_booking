package Customer;
import Schedular.Schedular_Login;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Customer_Login extends Schedular_Login {
    @Override
    public void log() {
        super.log();
    }

    @Override
    protected Boolean read_file(String email, String ID, String Password) {
        try (BufferedReader read = new BufferedReader(new FileReader("C:/Users/sagl3/OneDrive/Desktop/Java_Assignment/Java_Assignment/src/Customer/Customer/customer.txt"))) {
            String line;

            while ((line = read.readLine()) != null) {
                if (line.startsWith(ID) && 
                    ID.startsWith("CU") && 
                    line.contains(email) && 
                    line.contains(Password)) {
                    
                    // Additional check to ensure email and password are in correct positions
                    String[] parts = line.split(", ");
                    if (parts.length >= 6 && 
                        parts[3].equals(email) && 
                        parts[5].equals(Password)) {
                        return true;
                    }
                }
            }               
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        return false;
    }
}
