import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class smtp {
    
    

    public smtp(){
        
    }


    public void sendEmail(String Email) throws IOException {

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
            TimeUnit.MILLISECONDS.sleep(100);
            out.println("HELO smtp.afrihost.co.za");

        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        System.out.println(in.readLine());

        //mail-from
        try {
            TimeUnit.MILLISECONDS.sleep(100);
            out.println("mail from:danienel21@gmail.com");

        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        System.out.println(in.readLine());

        //rcpt to
        try {
            TimeUnit.MILLISECONDS.sleep(100);
            out.println("rcpt to:"+Email);

        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        System.out.println(in.readLine());

        //data
        try {
            TimeUnit.MILLISECONDS.sleep(100);
            out.println("data");

        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        System.out.println(in.readLine());

        //message
        try {
            TimeUnit.MILLISECONDS.sleep(100);
            out.println("Hi!");
            out.println("");
            out.println("I am currently on vaction.");


        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        

        //end message
        try {
            TimeUnit.MILLISECONDS.sleep(100);
            out.println(".");

        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        System.out.println(in.readLine());

        // quit
        try {
            TimeUnit.MILLISECONDS.sleep(100);
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