import java.util.Random;
import java.util.Scanner;
public class Schedular{
    public static void main(String [] args){
        availibility_schedule h=new availibility_schedule(null, null, null, 0, 0,null,null,null);
        h.set_data_time_Initially();
        // h.Hall_search_filter("Hall_Info.txt");
        // h.delete_Hall_info();
        // h.set_Hall_info();
        // h.update_Hall_info();
        // h.view_file("Hall_Info.txt");
        login log=new login();
        log.log();
    }

    
}
/*Register register=new Register();
        //register.identity();
        Random rand=new Random();

        int [] random_number=new int[6];

        for(int i=0; i< 6; i++){
         random_number[i]=rand.nextInt(100000, 999999);
             register.set_ID("CU"+random_number[i]);
             System.out.println(register.get_ID());
        }
        */