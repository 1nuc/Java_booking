package Schedular;

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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;


class availibility_schedule extends Hall_Management{

    private String Start_Date;
    private String End_Date;
    private String Remarks;
    private List<String> available_days;
    
    availibility_schedule(String Hall_ID, String Hall_Type, String Hall_Location, double cost_per_hour, int Hall_Capacity,String Start_Date, String End_Date, String Remarks){
        super(Hall_ID, Hall_Type, Hall_Location, cost_per_hour, Hall_Capacity);
        this.Start_Date=Start_Date;
        this.End_Date=End_Date;
        this.Remarks=Remarks;
        this.available_days=new ArrayList<>();
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

    // make encabsualtion for the available days of the halls depending on their type

    void set_available_days(List <String> available_days){
        this.available_days=available_days;
    }

     public List<String> get_available_days(){
        return available_days;
    }
    

    @Override
    public List<String> read_Hall_IDs(){
        
        List<String> list=new ArrayList<>();
        try (BufferedReader read = new BufferedReader(new FileReader("src/Schedular/Schedular/Hall_Info.txt"))) {
            String Lines;
            while((Lines=read.readLine())!=null){
                String [] line=Lines.split(", ");
                String id=line[0];
                String type=line[1];
                list.add(id+", "+type);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Error reading the file");
            e.printStackTrace();
        }
            return list;

    }   
    
    //creating lists methods to extract the Hall id with the type for setting availability and booking
    
    public List<String> Type_Of_Hall(){
        List<String>hall_information=new ArrayList<>();
        hall_information=read_Hall_IDs();
        List<String>Hall_Types=new ArrayList<>();

        for(String info: hall_information){
            String[] parts = info.split(", ");
            Hall_Types.add(parts[1]);

         }
        return Hall_Types;
    }

    public List<String> ID_Of_Hall(){
        List<String>hall_information=new ArrayList<>();
        hall_information=read_Hall_IDs();
        List<String>Hall_ID=new ArrayList<>();

        for(String info: hall_information){
            String[] parts = info.split(", ");
            Hall_ID.add(parts[0]);

         }
        return Hall_ID;
    }

    public void save_date_time_to_file(){
        

        List<String>ha_id=new ArrayList<>();
        List<String>ha_Type=new ArrayList<>();

        ha_id=ID_Of_Hall();
        ha_Type=Type_Of_Hall();
        try (PrintWriter write = new PrintWriter("src/Schedular/Schedular/Hall_Availibility.txt")) {

            for (int i=0; i <ha_id.size();i++){
                check_available_days(ha_Type.get(i));
                write.println(ha_id.get(i)+", "+ha_Type.get(i)+", "+ "Start Date"+", "+get_Start_Date()+", "+"End Date"+", "+get_End_Date()+
                ", "+"From "+get_available_days().get(0)+" To "+get_available_days().get(get_available_days().size() -1)+", ");

            }


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void specify_date_time(LocalDateTime [] date){

        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (Scanner input = new Scanner(System.in)) {

            //setting start and end date temprorirly to null
            String s_d=null;
            String e_d=null;
            
            while(date[0]==null){
                System.out.println("Enter a start date \nMake sure to use this format yyyy-MM-dd");
                s_d=input.nextLine();
                
                //for more validation check the input entered by the user
                if(!s_d.matches("\\d{4}-\\d{2}-\\d{2}") || !Year_Valid(s_d)){
                    System.out.println("Invalid format or year less than 2024...try again");
                }
                else{
                    //convert the format entered by the user to a localdate and time using localdatatime package in java
                    try {
                        LocalDate parsedate=LocalDate.parse(s_d, formatter);
                        date[0]=LocalDateTime.of(parsedate,LocalTime.of(8,0));
                        
                    } catch (DateTimeParseException e) {
                        System.out.println("An accurate date format. try again");
                        // TODO: handle exception
                    }

                }
                
            }
            while(date[1]==null){
                System.out.println("Enter an end date \nMake sure to use this format yyyy-MM-dd hh a");
                e_d=input.nextLine();
                
                if(!e_d.matches("\\d{4}-\\d{2}-\\d{2}") || !Year_Valid(e_d)){
                    System.out.println("Invalid format or year less than 2024...try again");
                }
                else{
                    try {
                        LocalDate parseDATE=LocalDate.parse(e_d, formatter);
                        date[1]=LocalDateTime.of(parseDATE,LocalTime.of(18,0));  
                        
                    } catch (DateTimeParseException e) {
                        System.out.println("Inaccurate date format. try again");
                        // TODO: handle exception
                    }

                }
                
            }
            if (!isStartDateBeforeEndDate(date[0], date[1])) {
                System.out.println("Start date must be before the end date. Please enter the dates again.");
                date[0] = null;
                date[1] = null;
                specify_date_time(date);  // Re-run the method to re-enter the dates
            }else{
                File file=new File("src/Schedular/Schedular/Hall_Maintenance.txt");
                file.delete();
                System.out.println("Maintenance record has been deleted please set the maintenance record again in the maintenance schedule section with the new date and time");
            }
        }
        
        
    }

    private Boolean Year_Valid(String date){
        int year=Integer.parseInt(date.substring(0,4));
        return year >=2024 ;
    }

    private boolean isStartDateBeforeEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate.isBefore(endDate);
    }


    public void set_date_time(){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd hh a");

        LocalDateTime []dates=new LocalDateTime[2];
        specify_date_time(dates);

        LocalDateTime start_date=dates[0];
        LocalDateTime end_date=dates[1];

        set_Start_Date(start_date.format(formatter));
        set_End_Date(end_date.format(formatter));
        save_date_time_to_file();
    
    }


    public void check_available_days(String Hall_Type){
        List<String> all_days =Arrays.asList("Monday","Tuesday","Wednesday","Thursday","Friday");
        List<String> work_days =Arrays.asList("Monday","Tuesday","Wednesday","Thursday");
        List<String> holiday_days =Arrays.asList("Friday","Saturday","Sunday");
            if(Hall_Type.equals("Auditorium")){
                set_available_days(all_days);
            }

            else if(Hall_Type.equals("Meeting_Room")){
                set_available_days(work_days);
            }

            else if(Hall_Type.equals("Banquet_Hall")){
                set_available_days(holiday_days);
            }

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
                Mark_maintenance(HA_id, M_Start_Date_Time, M_End_Date_Time);
                }
            }else{
                System.out.println("Hall ID not found");
            }
        }

    }

    public void Mark_maintenance(String ID, LocalDateTime startDate, LocalDateTime endDate){
        try (PrintWriter write = new PrintWriter(new FileWriter("src/Schedular/Schedular/Hall_Maintenance.txt",true))) {
            DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            write.println("Hall ID: "+ID+", "+"Maintenance Start Date: "+startDate.format(formatter)+", "+"Maintenance End Date: "+endDate.format(formatter));
            System.out.println("Maintenance Schedule marked successfully for the Hall with ID: "+ID);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

   private List<LocalDateTime[]> read_M_Date_Time(String HallID){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
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
                        System.out.println("Error parsing the maintenance date in line "+line);
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



    public void Specific_Hall_availability(){

        //call the method of setting the start and end date
        view_file("src/Schedular/Schedular/Hall_Availibility.txt");

        try (Scanner scan = new Scanner(System.in)) {
            DateTimeFormatter General_date_format=DateTimeFormatter.ofPattern("yyyy-MM-dd hh a");
             LocalDateTime start_date=LocalDateTime.parse(get_Start_Date(),General_date_format);
            LocalDateTime end_date=LocalDateTime.parse(get_End_Date(),General_date_format);

            DateTimeFormatter date_format=DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter time_format=DateTimeFormatter.ofPattern("hh:mm a");
            

            System.out.println("Enter Hall ID to display its own availability: ");
            String id=scan.nextLine();

            List<String>HallIDs=ID_Of_Hall();
            List<String>HallTypes=Type_Of_Hall();
            LocalDateTime M_S_Date=null;
            LocalDateTime M_E_Date=null;
            //check if the value exists by detrimining the number of index in the list
            int index=HallIDs.indexOf(id);

            // create a control structure if the index exits ....if the index ==-1 means the index not in the list
            if(index != -1){
                    // get the hall type of the same index of the hall id 
                    String h_type=HallTypes.get(index);

                    // set the available days depending on the hall type
                    check_available_days(h_type);

                    // set a list to track the available days in the list
                    List <String> Ava_days=get_available_days();

                    System.out.println("Hall ID: "+id+", "+ "Hall Type: "+h_type);
                    List<LocalDateTime[]> Maintenance_Range=read_M_Date_Time(id);


                    while(!start_date.isAfter(end_date)){
                            String Day_Name = start_date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                            Boolean Is_Under_Maintenance=false;
                            for(LocalDateTime[] range:Maintenance_Range){
                                if(!start_date.isBefore(range[0]) && !start_date.isAfter(range[1])){
                                    M_S_Date=range[0];
                                    M_E_Date=range[1];
                                    Is_Under_Maintenance=true;
                                    break;
                                }
                            }

                            if(Is_Under_Maintenance){
                                String availability= String.format("Date: %s, Day: %s, Under Maintenance from %s To %s",
                                start_date.format(date_format),
                                Day_Name,
                                M_S_Date.format(time_format), 
                                M_E_Date.format(time_format));
                                System.out.println(availability);

                            }
                            else if( Ava_days.contains(Day_Name)){
                                String availability= String.format("Date: %s, Day: %s, Available from %s To %s",
                                start_date.format(date_format),
                                Day_Name,
                                start_date.format(time_format), 
                                end_date.format(time_format));
                                System.out.println(availability);
                            }else{
                                String availability= String.format("Date: %s, Day: %s, Not available",
                                start_date.format(date_format),
                                Day_Name);
                                System.out.println(availability);
                            }
                            start_date=start_date.plusDays(1);
                        }
            }else{
                System.out.println("Not found");
            }
        }
        


    }
   //this is for the rest of roles
    //Make sure to use this function when reading any file either by inhereting it or applying polymoriphism
    @Override
    public void view_file(String FileName) {
        // TODO Auto-generated method stub
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(FileName))) {
                String Lines;
                String [] line=null;
                while((Lines=reader.readLine())!=null){
                    line=Lines.split(", ");
                }
                set_Start_Date(line[3]);
                set_End_Date(line[5]);
            }
        } catch (IOException e) {
            // TO Auto-generated catch block
            System.out.println("Error");
        }    }
    
}

public class Hall_Availibility{
    public static void main(String[] args){
        
        availibility_schedule h=new availibility_schedule(null, null, null, 0, 0,null,null,null);
    //  h.save_date_time_to_file();
//    h.Schedule_Maintenance();
    // h.set_date_time();
    h.Specific_Hall_availability();
        // h.Hall_search_filter("Hall_Info.txt");
        // h.delete_Hall_info();
        // h.set_Hall_info();
        // h.update_Hall_info();
       //  h.view_file("src/Schedular/Schedular/Hall_Availibility.txt");
        // login log=new login();
        //log.log();
       // h.read_M_Date_Time("HA991622");
        
    }
}
