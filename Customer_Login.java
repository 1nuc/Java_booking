import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Customer_Login extends login{
    @Override
    public void log(){
        super.log();
    }

    @Override
    protected Boolean read_file(String email, String ID, String Password){

        try(BufferedReader read= new BufferedReader(new FileReader("customer.txt"))){
            String lines;

            while((lines=read.readLine())!=null){
                String[] line=lines.split(", ");
                if(line[0].equals(ID) && line[0].startsWith("CU") && line[2].equals(email) && line[4].equals(Password)){
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

