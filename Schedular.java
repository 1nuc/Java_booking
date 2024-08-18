package Schedular;

public class Schedular{
    public static void main(String [] args){
        Hall_Management m=new Hall_Management(null, null, null, 0, 0);
        availibility_schedule h=new availibility_schedule(null, null, null, 0, 0,null,null,null, null, null, null);
        h.Specific_Hall_availability();
        //h.Schedule_Maintenance();
       // h.set_date_time();
        // h.Hall_search_filter("src/Schedular/Schedular/Hall_Info.txt");
        //m.delete_Hall_info();
         //h.set_Hall_info();
       // m.update_Hall_info();
        //  h.view_file("src/Schedular/Schedular/Hall_Info.txt");
        // Schedular_Login log=new Schedular_Login();
        // log.log();
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