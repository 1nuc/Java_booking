package Schedular;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Hall_Maintenance extends Hall_Availability {
    private String M_Start_Date;
    private String M_End_Date;
    private String M_Remarks;
    
    Hall_Maintenance(String Hall_ID, String Hall_Type,
     String Hall_Location, double cost_per_hour, int Hall_Capacity,String Start_Date,
     String End_Date, List<String> available_days, 
     String M_Start_Date, String M_End_Date, String M_Remarks){
        super(Hall_ID, Hall_Type, Hall_Location, cost_per_hour, Hall_Capacity, Start_Date, End_Date, available_days);

        this.M_Start_Date=Start_Date;
        this.M_End_Date=End_Date;
        this.M_Remarks=M_Remarks;
    }

    void set_M_Start_Date(String M_Start_Date){
        this.M_Start_Date=M_Start_Date;
    } 
    public String get_M_Start_Date(){
        return M_Start_Date;
    }


    void set_M_End_Date(String End_Date){
        this.M_End_Date=End_Date;
    } 
    public String get_M_End_Date(){
        return M_End_Date;
    }

    void set_M_Remarks(String M_Remarks){
        this.M_Remarks=M_Remarks;
    } 

    public String get_M_Remarks(){
        return M_Remarks;
    }

    public void Schedule_Maintenance(){

        view_file("src/Schedular/Schedular/Hall_Availibility.txt");
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd hh a");
        DateTimeFormatter dateformat=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeformat=DateTimeFormatter.ofPattern("hh a");

        List<String>Hall_IDs=ID_Of_Hall();
        List<String>Hall_type=Type_Of_Hall();
        LocalDateTime M_Start_Date_Time=null;
        LocalDateTime M_End_Date_Time=null;

        try (Scanner scan = new Scanner(System.in)) {
            System.out.println("Enter Hall ID to set to schedule maintenance: ");
            String HA_id=scan.nextLine();

            int index=Hall_IDs.indexOf(HA_id);
            if(index!=-1){

                 String HA_Type=Hall_type.get(index);
                 check_available_days(HA_Type);
                
                while(M_Start_Date_Time==null){
                    System.out.println("Enter the maintenance start date (yyyy-MM-dd): ");  
                    String M_Start_Date = scan.nextLine();
                    System.out.println("Enter the maintenance start time (hh:mm AM/PM): ");
                    String M_Start_Time = scan.nextLine();

                    System.out.println("Enter the reason for Maintenance (Remarks)");
                    set_M_Remarks(scan.nextLine());

                    if(!M_Start_Date.matches("\\d{4}-\\d{2}-\\d{2}") || !Year_Valid(M_Start_Date)){
                        System.err.println("Invalid date format");

                    }else{
                        try {
                            LocalDate parsedDate = LocalDate.parse(M_Start_Date, dateformat);
                            LocalTime parsedTime = LocalTime.parse(M_Start_Time, DateTimeFormatter.ofPattern("hh:mm a"));
                            M_Start_Date_Time = LocalDateTime.of(parsedDate, parsedTime);

                        } catch (DateTimeParseException e) {
                            System.out.println("Incorrect date format entered");
                            // TODO: handle exception
                        }

                    }
                }

                while(M_End_Date_Time==null){
                    
                    System.out.println("Enter the maintenance End date (yyyy-MM-dd): ");
                    String M_End_Date = scan.nextLine();
                    System.out.println("Enter the maintenance start time (hh:mm AM/PM): ");
                    String M_End_Time = scan.nextLine();
                    
                    if(!M_End_Date.matches("\\d{4}-\\d{2}-\\d{2}") || !Year_Valid(M_End_Date)){
                        System.err.println("Invalid date format");
                    }else{
                        try {
                            LocalDate parsedDate = LocalDate.parse(M_End_Date, dateformat);
                            LocalTime parsedTime = LocalTime.parse(M_End_Time, DateTimeFormatter.ofPattern("hh:mm a"));
                            M_End_Date_Time = LocalDateTime.of(parsedDate, parsedTime);
                            
                        } catch (DateTimeParseException e) {
                            // TODO: handle exception
                            System.out.println("Incorrect date format entered");
                        }
                    }

   
                }

                LocalDateTime startDate = LocalDateTime.parse(get_Start_Date(), formatter);
                LocalDateTime endDate = LocalDateTime.parse(get_End_Date(), formatter);

                if (M_Start_Date_Time.isBefore(startDate) || M_End_Date_Time.isAfter(endDate)) {
                    System.out.println("Maintenance time must be within the availability range.");
                    Schedule_Maintenance();
                } else if (!M_Start_Date_Time.isBefore(M_End_Date_Time)) {
                    System.out.println("Maintenance start time must be before the end time.");
                    Schedule_Maintenance();

                }else{

                System.out.println("Hall ID: " + HA_id + ", Hall Type: " + HA_Type);
                System.out.println("From: " + M_Start_Date_Time.format(dateformat) + " " + M_Start_Date_Time.format(timeformat));
                System.out.println("To: " + M_End_Date_Time.format(dateformat) + " " + M_End_Date_Time.format(timeformat));
                
                //store the start and end date and time into the set modifier
                set_M_Start_Date(M_Start_Date_Time.format(formatter));
                set_M_End_Date(M_End_Date_Time.format(formatter));

                Mark_maintenance(HA_id);
                }
            }else{
                System.out.println("Hall ID not found");
            }
        }

    }

    public void Mark_maintenance(String ID){

        try (PrintWriter write = new PrintWriter(new FileWriter("src/Schedular/Schedular/Hall_Maintenance.txt",true))) {
            write.println("Hall ID: "+ID+", "+"Maintenance Start Date: "+get_M_Start_Date()+
            ", "+"Maintenance End Date: "+get_M_End_Date()+", "+"Remarks: "+get_M_Remarks());
            System.out.println("Maintenance Schedule marked successfully for the Hall with ID: "+ID);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

   protected List<LocalDateTime[]> read_M_Date_Time(String HallID){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd hh a");
        List<LocalDateTime []> M_Periods=new ArrayList<>();

        try (BufferedReader read = new BufferedReader(new FileReader("src/Schedular/Schedular/Hall_Maintenance.txt"))) {
            String Lines;
            while ((Lines=read.readLine())!=null) {
                String[] line=Lines.split(": |, ");
                if(line[1].equals(HallID)){
                    try{
                        LocalDateTime M_St_Date=LocalDateTime.parse(line[3], formatter);
                        LocalDateTime M_En_Date=LocalDateTime.parse(line[5], formatter);
                        M_Periods.add(new LocalDateTime[]{M_St_Date, M_En_Date});
                        System.out.println();
                    }catch(Exception e){
                        System.out.println("Error parsing the maintenance date in line \n"+Arrays.toString(line));
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Error reading the file");
        }
        return M_Periods;

    }

}
