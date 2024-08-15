import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

class Customer_Register extends User_Management {
    // Constructor
    public Customer_Register(String Name, String Email, String ID, String Role,String Password) {
        super(Name, Email, ID, Role, Password);
    }

    @Override
    
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

    private void Role_checking(){//A function that checks the ID to identify the role

        if(get_ID().startsWith("CU")){
            set_Role("Customer");
        }


    }
    private boolean ID_checking(){//A function that checks the Syntax of the ID 

            if(Pattern.matches("^(CU)\\d{6}$", get_ID())){
                return true;
            }

            else{
                System.out.println("ID not valid. The ID must start with CU followed by exactly six digits.");
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

        public void SaveToFile(){//saving all the info entered to a speciliazed file

        try{

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("customer.txt",true))) {
                writer.write(get_ID()+", "+get_Name()+", "+get_Email()+", "+get_Role()+", "+get_Password()+", ");
                writer.newLine();
            }

        }catch(IOException e){
            System.out.println("An error occured writing to the file");
        }
    }

}