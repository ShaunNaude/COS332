import java.io.*;
import java.net.*;
import java.util.*;

public class webserver extends Thread {
    public static class questionObj{
            public String text;
            public ArrayList<answerObj> answers = new ArrayList<answerObj>();
        }
        public static class answerObj{
            public String text;
            public boolean status;
        }
        
        public static ArrayList<questionObj> questions = new ArrayList<questionObj>();
        public static String arr[] = new String[25] ;
        
        public static void main(String argv[]) throws Exception
        {
            //Reading current dataBase
            BufferedReader fileReader = null;
            fileReader = new BufferedReader(new FileReader("database.txt"));
            
            String sCurrentLine;
            
            int num=0;
            
            while ((sCurrentLine = fileReader.readLine()) != null) {
                
                arr[num] = sCurrentLine;
                num++;
            }
            
            
            new webserver().start();
        } 
        
        public void run() {
             try {
                ServerSocket server = new ServerSocket(11111);
                System.out.println("Webserver started");
                System.out.println("Press ctrl+c to stop");
                System.out.println("");
                boolean shutdown = false;
                    
                while (!shutdown) {               
                    Socket socket = server.accept();                
                    PrintWriter out = new PrintWriter(socket.getOutputStream());            
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));              
                    String line = "";
                    String request = "";
                    
                    //Get request from client
                    int length = -1;
                    while ((line = in.readLine()) != null && (line.length() != 0)) {
                        //System.out.println(line);
                        if (line.indexOf("GET") > -1) {
                        //here i need to figure-out what is being requested
                        int start = line.indexOf("/");
                        int end = line.indexOf("H");
                        request = line.substring(start, end);
                        



                        }
                    }
                    
                    System.out.println(request);

                    String reply = "";
                    if (request.indexOf("answer") > -1){
                        //check if correct, ask to continue
                        reply = "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>COS 332 Quiz</title></head><body><form method='POST' action='' ><h1>COS 332 Browser quiz</h1>";

                        if (request.indexOf("answer=true") > -1){
                            reply += "<h1 style='color: green'>Correct</h1>";
                        }
                        else {
                            reply += "<h1 style='color: red'>Wrong</h1>";
                        }
                        
                        reply += "<h2>Would you like to continue</h2><input type='radio' name='continue' value='YES'> YES <br><input type='radio' name='continue' value='NO'> NO <br><input type='submit' value='Submit'></form></body>";
                    }
                    else {
                        //display questions, check if continue
                        if (request.indexOf("continue=NO") > -1) {
                            shutdown = true;
                        }
                        
                        //here we need to make our normal index.html
                        //generate page
                         reply = "<!DOCTYPE html><html><body><h2>HTML Forms</h2><form action='http://www.localhost:11111/'><label for='l1'>Task:</label><br><input type='text' id='ftask' name='ftask' value=''><br><label for='l2'>Input:</label><br><input type='text' id='finput' name='finput' value=''><br><br><input type='submit' value='Submit'></form> </body></html>";
                        //reply += "<h2>" + questions.get(n).text + "</h2>";    

                        
                    }
                    
                    if (!shutdown)
                    {
                        out.println("HTTP/1.0 200 OK");
                        out.println("Content-Type: text/html; charset=utf-8");
                        out.println("");
                        out.println(reply);
                    }
                    
                    out.close();
                    socket.close();
                }
                server.close();            
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
