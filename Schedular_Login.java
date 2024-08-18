package Schedular;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

abstract class Schedular_log{
    abstract public void log();
    abstract protected Boolean read_file(String email, String ID, String Password);
}
public class Schedular_Login extends Schedular_log{
    @Override
    public void log(){
        try(Scanner input = new Scanner(System.in)){
        
            System.out.println("Email:");
            String email=input.nextLine();

            System.out.println("ID:");
            String ID=input.nextLine();

            System.out.println("Password");
            String password=input.nextLine();
            read_file(email, ID, password);

            Boolean is_found=read_file(email, ID, password);
        

            if(is_found) {
                System.out.println("Login Successfully");
            }
            else{
            System.out.println("Login Failed");
            } 
            input.close();
        }catch(NoSuchElementException e){
            System.out.println("No more input available");
        }
       
    }

    protected Boolean read_file(String email, String ID, String Password){

        try(BufferedReader read= new BufferedReader(new FileReader("src/Adminstrator/Database.txt"))){
            String lines;

            while((lines=read.readLine())!=null){
                String[] line=lines.split(", ");
                if(line[0].equals(ID) && line[0].startsWith("SC") && line[2].equals(email) && line[4].equals(Password)){
                    return true;
                }
               
            }               
        }catch (IOException e) {
            // TO: handle exception
            System.out.println("Error reading the file");
        }
        return false;

    }
}

