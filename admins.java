import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;


class Adding_Info{
    private String Name;
    private String Email;
    private String ID;
    private String Role;
    private String Password;

//creating protected varuables to store the information of each role efficiently

    public void Set_info(){
        //the assinging of each Role
        try (Scanner scan = new Scanner(System.in)) {
            System.out.println("Name:");
            Name=scan.nextLine();
            
            Boolean Valid_Email=false;//Checking the syntax of each email entered by the user
            while(!Valid_Email){
                System.out.println("Email:");
                Email=scan.nextLine();
                Valid_Email=email_checking();
            }
    
            Boolean valid_ID=false;
            while(!valid_ID){//checking the type of the ID entered to match the requirement
                System.out.println("ID:");
                ID=scan.nextLine();
                valid_ID=ID_checking();
            }
            Role_checking();//call for the function that checks the Role of the user entered by the ID

            System.out.println("Password:");
            Password=scan.nextLine();
        }

        SaveToFile();
    }
/* This can be imlemented in future use 
    public String get_ID(){
        return ID;
    }
    public String get_Name(){
        return get_Name();
    }
    public String get_Email(){
        return Email;
    }
    public String get_Role(){
        return Role;
    }
    public String get_Password(){
        return Password;
    }
*/
    private void SaveToFile(){//saving all the info entered to a speciliazed file

        try{

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Database.txt",true))) {
                writer.write(ID+", "+Name+", "+Email+", "+Role+", "+Password+",");
                writer.newLine();
            }

        }catch(IOException e){
            System.out.println("An error occured writing to the file");
        }
    }
    
    private void Role_checking(){//A function that checks the ID to identify the role

        if(ID.startsWith("MA")){
            this.Role="Manager";
        }

        else if(ID.startsWith("CU")){
            this.Role="Customer";
        }

        else if(ID.startsWith("SC")){
            this.Role="Schedular";
        }

        else if(ID.startsWith("AD")){
            this.Role="Administrator";
        }
    }

    private boolean ID_checking(){//A function that checks the Syntax of the ID 

            if(Pattern.matches("^(MA|CU|SC|AD)\\d{6}$", ID)){
                return true;
            }

            else{
                System.out.println("ID not valid. The ID must start with MA, ST, SC, or AD followed by exactly six digits.");
                return false;
        }
    }
    
    private Boolean email_checking(){//A function that checks the syntax of the Email entered

        if(Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", Email)){
            return true;
        }
        else{
            System.out.println("incorrect Email entered try again");
            return false;
        }
    }

}

//Applying inheretience for further imporvments in the code
class Register extends Adding_Info{

    public void identity(){
        Set_info();
    }
}