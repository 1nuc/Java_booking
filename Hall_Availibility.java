import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Calendar;


abstract class Schedular_Login{
    abstract public void log();
    abstract protected Boolean read_file(String email, String ID, String Password);
}

class login extends Schedular_Login{
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

        try(BufferedReader read= new BufferedReader(new FileReader("Database.txt"))){
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



class availibility_schedule extends Hall_Management{

    private String Start_Date;
    private String End_Date;
    private String Remarks;
    
    availibility_schedule(String Hall_ID, String Hall_Type, String Hall_Location, double cost_per_hour, int Hall_Capacity,String Start_Date, String End_Date, String Remarks){
        super(Hall_ID, Hall_Type, Hall_Location, cost_per_hour, Hall_Capacity);
        this.Start_Date=Start_Date;
        this.End_Date=End_Date;
        this.Remarks=Remarks;
    }
    
    void set_Start_Date(String Start_Date){
        this.Start_Date=Start_Date;
    } 
    public String get_Start_Date(){
        return Start_Date;
    }


    void set_End_Date(String End_Date){
        this.End_Date=End_Date;
    } 
    public String get_End_Date(){
        return End_Date;
    }


    void set_Remarks(String Remarks){
        this.Remarks=Remarks;
    } 
    public String get_Remarks(){
        return Remarks;
    }
    

    @Override
    public List<String> read_Hall_IDs(){
        
        List<String> list=new ArrayList<>();
        try (BufferedReader read = new BufferedReader(new FileReader("Hall_Info.txt"))) {
            String Lines;
            while((Lines=read.readLine())!=null){
                String [] line=Lines.split(", ");
                list.add(line[0]);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Error reading the file");
            e.printStackTrace();
        }
            return list;

    }   

    public void set_data_time_Initially(){

        String St_Date;
        String En_Date;

        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.YEAR,2024);
        cal.set(Calendar.HOUR_OF_DAY,8);
        cal.set(Calendar.MONTH,Calendar.JUNE);
        

        //Setting the start date initially
        St_Date=cal.get(Calendar.DAY_OF_MONTH)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.YEAR)+" "+cal.get(Calendar.HOUR)+" AM";
        //Store it in the Set Start date method
        set_Start_Date(St_Date);

        //Setting the End data initially as well
        cal.set(Calendar.HOUR_OF_DAY,18);
        En_Date=cal.get(Calendar.DAY_OF_MONTH)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.YEAR)+" "+cal.get(Calendar.HOUR)+" PM";
        //Store it in the Start Date initially as well
        set_End_Date(En_Date);

        List<String>Hall_IDs=read_Hall_IDs();

        try (PrintWriter write = new PrintWriter("Hall_Availibility.txt")) {

            for (String ID:Hall_IDs){
                
                write.println(ID+", "+ "Start Date"+", "+get_Start_Date()+", "+"End Date"+", "+get_End_Date());
            }


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    //this is for the rest of roles
    //Make sure to use this function when reading any file either by inhereting it or applying polymoriphism
    @Override
    public void view_file(String FileName) {
        // TODO Auto-generated method stub
        super.view_file(FileName);
    }
    
}

public class Hall_Availibility{
    public static void main(String[] args){
        
        availibility_schedule h=new availibility_schedule(null, null, null, 0, 0,null,null,null);
        //h.set_data_time_Initially();
        // h.Hall_search_filter("Hall_Info.txt");
        // h.delete_Hall_info();
        // h.set_Hall_info();
        // h.update_Hall_info();
        // h.view_file("Hall_Info.txt");
        // login log=new login();
        //log.log();
    }
}