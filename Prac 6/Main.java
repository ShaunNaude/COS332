import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.text.SimpleDateFormat;  

public class Main {
    public static void main(String[] args) throws IOException {

        //create todays date as string
        LocalDate currentdate = LocalDate.now();
        String today = Integer.toString(currentdate.getYear())+"-0"+Integer.toString(currentdate.getMonthValue())+"-"+Integer.toString(currentdate.getDayOfMonth());

        String[] events = new String[100];
        String[] upcomingEvents = new String[100];

        //read events from file, 
        File myObj = new File("dates.txt");
        Scanner myReader = new Scanner(myObj);
        int counter = 0;
        while (myReader.hasNextLine()) {
         events[counter] = myReader.nextLine();
        counter++;
        }
        myReader.close();

        //get dates from events array
        int num = 0;;
        for(int i = 0 ; i<counter ; i++)
        {
            //get dates from array
            String date = events[i].substring(0,5);
            date = "2020-"+date.substring(3,5)+"-"+date.substring(0,2);

            //parse dates
            LocalDate dateBefore = LocalDate.parse(date);
            LocalDate dateAfter = LocalDate.parse(today);

            //calc days between dates
            long noOfDaysBetween = ChronoUnit.DAYS.between(dateAfter,dateBefore);

            //send emails to myself if <=6
            if(noOfDaysBetween<=6)
            {
                upcomingEvents[num] = events[i].substring(5,events[i].length()); 
                num++;
            }
            
        }

       

        
       

        //set-up socket and buffers
        Socket pingSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
     
        try {
            pingSocket = new Socket("smtp.afrihost.co.za", 25);
            out = new PrintWriter(pingSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(pingSocket.getInputStream()));
        } catch (IOException e) {
            return;
        }

       if(in.readLine() != "")
       {
        //HELO
        try {
            TimeUnit.MILLISECONDS.sleep(300);
            out.println("HELO smtp.afrihost.co.za");

        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        System.out.println(in.readLine());

        //mail-from
        try {
            TimeUnit.MILLISECONDS.sleep(300);
            out.println("mail from:Reminder@gmail.com");

        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        System.out.println(in.readLine());

        //rcpt to
        try {
            TimeUnit.MILLISECONDS.sleep(300);
            out.println("rcpt to:u18014080@tuks.co.za");

        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        System.out.println(in.readLine());

        //data
        try {
            TimeUnit.MILLISECONDS.sleep(300);
            out.println("data");

        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        System.out.println(in.readLine());

        //message
        try {
            TimeUnit.MILLISECONDS.sleep(300);
            out.println("Upcoming Events:");

            for(int i = 0 ; i<num ;i++)
            {
            out.println(upcomingEvents[i]);
            }

        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        

        //end message
        try {
            TimeUnit.MILLISECONDS.sleep(300);
            out.println(".");

        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        System.out.println(in.readLine());

        // quit
        try {
            TimeUnit.MILLISECONDS.sleep(300);
            out.println("quit");

        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        System.out.println(in.readLine());




       }
     
        
       
        
     
     
        out.close();
        in.close();
        pingSocket.close();

       
        
    }
}
