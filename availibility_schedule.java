package Schedular;


import java.io.File;
import java.io.FileNotFoundException;

import java.io.PrintWriter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.Locale;



public class availibility_schedule extends Hall_Maintenance{

    
    availibility_schedule(String Hall_ID, String Hall_Type, String Hall_Location, 
    double cost_per_hour, int Hall_Capacity,String Start_Date, 
    String End_Date, List<String> available_days, String M_Start_Date, String M_End_Date, String M_Remarks){
        super(Hall_ID, Hall_Type, Hall_Location, cost_per_hour, Hall_Capacity, Start_Date, End_Date, available_days,  M_Start_Date, M_End_Date, M_Remarks);
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
                                if( !start_date.isBefore(range[0]) && !start_date.isAfter(range[1]) ){
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
}
