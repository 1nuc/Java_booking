package Schedular;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.List;

public class Hall_Management extends Hall_info{

   public Hall_Management(String Hall_ID, String Hall_Type, String Hall_Location, double cost_per_hour, int Hall_Capacity){
        super(Hall_ID, Hall_Type, Hall_Location, cost_per_hour, Hall_Capacity);
    }
    

    public void set_Hall_info(){

        Scanner scan=new Scanner(System.in);

        Boolean hl=false;

        while(!hl){
            System.out.println("Enter Hall ID: ");
            setHall_ID(scan.nextLine().toUpperCase());
            hl=Hall_ID_checking();
        }

        Boolean check=false;
        while(!check){
            System.out.println("Enter Hall Type:\n(1- Auditorium, 2- Banquet_Hall, 3- Meeting_Room) :");
            if(scan.hasNextInt()){
                setHall_Type(scan.nextLine());
                check=Hall_Type_checking();
            }else{
                System.out.println("You must only input a number\"");
                scan.next();
            }
        }

        System.out.println("Enter Hall Location: ");
        setHall_location(scan.nextLine());

        set_Capacity_Price();
        Save_Hall_Info();
        scan.close();

    }

    private Boolean Hall_Type_checking(){
        try{
            int Type=Integer.parseInt(getHall_Type());

            switch (Type) {
                case 1:
                    setHall_Type("Auditorium");
                    return true;
                case 2:
                    setHall_Type("Banquet_Hall");
                    return true;
                case 3:
                    setHall_Type("Meeting_Room");
                    return true;
                default:
                    System.out.println("Invalid input");
                    return false;
            }

        }catch(NumberFormatException e){
            System.out.println("You must only enter a number");
            return false;
        }
        //check the type of the Hall
    }
 
    private boolean Hall_ID_checking(){//A function that checks the Syntax of the ID 

            if(Pattern.matches("^(HA)\\d{6}$", getHall_ID())){
                return true;
            }

            else{
                System.out.println("ID not valid. The ID must start with HA followed by exactly six digits.");
                return false;
        }
    }

    private void set_Capacity_Price(){
        if (getHall_Type().equals("Auditorium")){
            setHall_Capacity(1000);
            setcost_per_hour(300.00);
        }

        if (getHall_Type().equals("Banquet_Hall")){
            setHall_Capacity(300);
            setcost_per_hour(100.00);
        }
        
        if (getHall_Type().equals("Meeting_Room")){
            setHall_Capacity(30);
            setcost_per_hour(50.00);
        }
    }

   
    private void Save_Hall_Info(){//saving all the info entered to a speciliazed file
        try{
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/Schedular/Schedular/Hall_Info.txt",true))) {
                writer.write(getHall_ID()+", "+getHall_Type()+", "+getHall_location()+", "+getHall_Capacity()+", "+getcost_per_hour()+", ");
                writer.newLine();
                System.out.println("Hall information has been added successfully");
            }
        }catch(IOException e){
            System.out.println("An error occured writing to the file");
        }
    }
    
    public void view_file(String FileName){
            try {
                try (BufferedReader reader = new BufferedReader(new FileReader(FileName))) {
                    String Lines;
                    while((Lines=reader.readLine())!=null){
                        System.out.println(Lines);
                    }
                }
            } catch (IOException e) {
                // TO Auto-generated catch block
                System.out.println("Error");
            }
    }


    public void update_Hall_info(){
        //initializing the scanner to read the input
        try (Scanner scan = new Scanner(System.in)) {
            try { //initializing the file for reading and creating a new temp file to update the date.
                BufferedReader reader=new BufferedReader(new FileReader("src/Schedular/Schedular/Hall_Info.txt"));
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/Schedular/Schedular/Hall_Info_temp.txt",true))) {

                    //view the existing file information for the user to select which hall to update.
                    view_file("src/Schedular/Schedular/Hall_Info.txt");

                    System.out.println("Enter the Hall ID of which you want to update: ");;
                    String choice=scan.nextLine();

                    //some variables initializations that will be used in the section below.
                    int chn;
                    String lines;
                    Boolean Hall_found =false;
                    Boolean check=false;//to check the id given of the Hall
                    Boolean Type_check=false;//to check the type of the Hall given

                    //creating a while loop to read the data of the file to a strin named lines
                    while((lines=reader.readLine())!=null){
                        //splitting the lines and store them inside a string array
                        String[] line=lines.split(", ");

                        //making a condition to see if the Hall ID which is stored in the array index [0] matches the user input
                        if(choice.equals(line[0])){

                            Hall_found=true;//Boolean value that set to track the exitense of the Hall ID

                            System.out.println("The current Hall details are:\n"+ Arrays.toString(line));

                            System.out.println("Which field you want to update:\n(1-Hall ID), (2-Hall Type), (3- Hall Location)");
                            while(!scan.hasNextInt()){
                                System.out.println("invalid input please enter a valid number from the options above: ");
                                scan.next();
                            }
                            chn=scan.nextInt();
                            

                            scan.nextLine();//consume the last line
                            //updating the data 
                            switch(chn){
                                case 1:                            
                                while(!check){
                                    System.out.println("Enter new Hall ID: ");
                                    setHall_ID(scan.nextLine());
                                    check=Hall_ID_checking();
                                }
                                    line[0]=getHall_ID();
                                    break;

                                case 2:
                                    while(!Type_check){
                                        System.out.println("Enter the new Hall Type\n(1- Auditorium, 2- Banquet_Hall, 3- Meeting_Room): ");
                                        if(scan.hasNextInt()){
                                            setHall_Type(scan.nextLine());
                                            Type_check=Hall_Type_checking();
                                            set_Capacity_Price();
                                            line[1]=getHall_Type();
                                            line[3]=Integer.toString(getHall_Capacity());
                                            line[4]=Double.toString(getcost_per_hour());
                                        }else{
                                            System.out.println("You must only input a number");
                                            scan.next();
                                        }
                                    }                             
                                    break;

                                case 3:                                    
                                    System.out.println("Enter new Hall Location");
                                    setHall_location(scan.nextLine());
                                    line[2]=getHall_location();
                                    break;

                                default:
                                    System.out.println("Please enter a valid number");
                                    break;
                                //key features
                                //1- temp file creation
                                //2- write directly to temp file 
                                //3- copy the rest of the data of the orgin file to the temp file
                            }
                            writer.write(String.join(", ", line)+", ");
                            writer.newLine();
                        }
                        //copy the rest of the unmodified code to the file
                        else{
                            writer.write(lines);
                            writer.newLine();
                        }
                        
                    }//exiting the while loop
                    //if the Hall ID is not in the file then writing back all the line information back to the file
                    //The Boolean value that set to track Hall ID 
                    if(!Hall_found){
                        System.out.println("Hall not found");
                    }
                    else{
                        System.out.println("Hall infromation have been successfully updated");
                    }

                    //closing the reader and writer for the both file 
                    reader.close();
                    writer.close();

                    //deleting the actual file 
                    File oldFile= new File("src/Schedular/Schedular/Hall_Info.txt");
                    oldFile.delete();

                    //renaming the temp file with the name of the actual file
                    File new_file= new File("src/Schedular/Schedular/Hall_Info_temp.txt");
                    new_file.renameTo(oldFile);
                }

            } catch (FileNotFoundException e) {
                // TO Auto-generated catch block
                System.out.println("unable to detect the file");
            } catch (IOException e) {
                // TO Auto-generated catch block
                System.out.println("unable to detect the file");
            }
            scan.close();
        }
        
    }


    public void delete_Hall_info(){
        Path filePath=Paths.get("src/Schedular/Schedular/Hall_Info.txt");
        try{

            List<String> lines=Files.readAllLines(filePath);
            String ID_Delete;
            Boolean found = false;
            String choice;

            try (Scanner scan = new Scanner(System.in)) {
                view_file("src/Schedular/Schedular/Hall_Info.txt");

                System.out.println("Enter the ID of Hall to be deleted: ");
                ID_Delete=scan.nextLine();
                
                for(int i=0; i< lines.size(); i++){
                    String line=lines.get(i);
                    String [] parts=line.split(", ");

                    if(parts[0].equals(ID_Delete.toUpperCase())){
                        
                        found=true;
                        System.out.println(Arrays.toString(parts));
                        System.out.println("Are you sure you want to delete\n(y)for yes, (n)for no: ");
                        choice=scan.nextLine();

                        switch(choice.toLowerCase()){
                            case "y":
                                lines.remove(i);
                                break;

                            case "n":
                                System.out.println("Thanks for using our service");
                                return;

                            default:
                                System.out.println("Invalid input");
                                break;
                        }
                    }
                }
                if(found){
                    Files.write(filePath,lines);
                    System.out.println("Hall information has been successfully deleted");
                }else{
                    System.out.println("Hall info not found");
                }
                scan.close();
            }
            

        }catch(IOException e){
            System.out.println("error reading the file");
        }
    }
   //this method can be used for all files
    public void Hall_search_filter(String FileName){

        try (Scanner input = new Scanner(System.in)) {
            Path FilePath=Paths.get(FileName);

            try{

                List<String> file=Files.readAllLines(FilePath);  
                String search;
                Boolean find=false;

                    System.out.println("Type or input anything to search for avaliable Hall inforamtion: ");
                    search=input.nextLine();

                    for(String line: file){
                        
                        if(line.contains(search)){
                            find=true;
                            System.out.println(line);
                        }
   
                    }
                    if(!find){
                        System.out.println("Hall infromation not found");
                    }

            }catch(IOException e){
                System.out.println("Error reading the file");
            }
            input.close();
        }
    }

    public List<String> read_Hall_IDs(){
        List<String> list=new ArrayList<>();
        return list;
    }   
       
}
    
