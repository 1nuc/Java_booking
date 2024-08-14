import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;


class User_Management{
    private String Name;
    private String Email;
    private String ID;
    private String Role;
    private String Password;
//applying encabsulation
    User_Management(String Name, String Email, String ID, String Role,String Password){
        this.Name=Name;
        this.Email=Email;
        this.ID=ID;
        this.Role=Role;
        this.Password=Password;
    }

    public void set_ID(String ID){
        this.ID=ID;
    }
    
    public String get_ID(){
        return ID;
    }
    //------------------------

    public void set_Name(String Name){
        this.Name=Name;
    }
    public String get_Name(){
        return Name;
    }
    //------------------------
    public void set_Email(String Email){
        this.Email=Email;
    }
    public String get_Email(){
        return Email;
    }
    //------------------------
    public void set_Role(String Role){
        this.Role=Role;
    }
    public String get_Role(){
        return Role;
    }
    //------------------------
    public void set_Password(String Password){
        this.Password=Password;
    }

    public String get_Password(){
        return Password;
    }
    //------------------------
//creating protected varuables to store the information of each role efficiently

    public void Set_info(){
        //the assinging of each Role
        try (Scanner scan = new Scanner(System.in)) {
            System.out.println("Name:");
            set_Name(scan.nextLine());
            
            Boolean Valid_Email=false;//Checking the syntax of each email entered by the user
            while(!Valid_Email){
                System.out.println("Email:");
                set_Email(scan.nextLine());
                Valid_Email=email_checking();
            }
    
            Boolean valid_ID=false;
            while(!valid_ID){//checking the type of the ID entered to match the requirement
                System.out.println("ID:");
                set_ID(scan.nextLine());
                valid_ID=ID_checking();
            }
            Role_checking();//call for the function that checks the Role of the user entered by the ID

            System.out.println("Password:");
            set_Password(scan.nextLine());
        }

        SaveToFile();
        System.out.println("Successfully assigned");
    }
 
    private void SaveToFile(){//saving all the info entered to a speciliazed file

        try{

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Database.txt",true))) {
                writer.write(get_ID()+", "+get_Name()+", "+get_Email()+", "+get_Role()+", "+get_Password()+", ");
                writer.newLine();
            }

        }catch(IOException e){
            System.out.println("An error occured writing to the file");
        }
    }
    
    private void Role_checking(){//A function that checks the ID to identify the role

        if(get_ID().startsWith("MA")){
            set_Role("Manager");
        }

        else if(get_ID().startsWith("CU")){
            set_Role("Customer");
        }

        else if(get_ID().startsWith("SC")){
            set_Role("Schedular");
        }

        else if(get_ID().startsWith("AD")){
            set_Role("Administrator");
        }
    }

    private boolean ID_checking(){//A function that checks the Syntax of the ID 

            if(Pattern.matches("^(MA|CU|SC|AD)\\d{6}$", get_ID())){
                return true;
            }

            else{
                System.out.println("ID not valid. The ID must start with MA, SC, or AD followed by exactly six digits.");
                return false;
        }
    }
    
    private Boolean email_checking(){//A function that checks the syntax of the Email entered

        if(Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", get_Email())){
            return true;
        }
        else{
            System.out.println("incorrect Email entered try again");
            return false;
        }
    }

}

//Applying inheretience for further imporvments in the code
class Register extends User_Management{
    Register(String Name, String Email, String ID, String Role,String Password){
        super(Name, Email, ID, Role, Password);
    }
    public void identity(){
        Set_info();
        System.out.println();
    }
}