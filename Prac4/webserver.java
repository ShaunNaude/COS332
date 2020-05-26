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
        public static Vector arr = new Vector() ;
        
        
        public static void main(String argv[]) throws Exception
        {
            //Reading current dataBase
            BufferedReader fileReader = null;
            fileReader = new BufferedReader(new FileReader("database.txt"));
            
            String sCurrentLine;
            
            
            
            while ((sCurrentLine = fileReader.readLine()) != null) {
                
                arr.add(sCurrentLine);
              
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
                    String task = "";
                    String input = "";
                    
                    //Get request from client
                    
                    while ((line = in.readLine()) != null && (line.length() != 0)) {
                        //System.out.println(line);
                        if (line.indexOf("GET") > -1) {
                        //here i need to figure-out what is being requested
                        int start = line.indexOf("/");
                        int end = line.indexOf("H");
                        request = line.substring(start, end);
                        
                        //here i get the task and input 
                        if(request.indexOf("ftask") > -1)
                        {
                            //so here i need to get the two input varibales
                            int taskPos = request.indexOf("=");
                            int splitPos = request.indexOf("&");
                            task = request.substring(taskPos+1, splitPos);

                            int inputPos = request.lastIndexOf("=");
                            input = request.substring(inputPos+1,request.length()-1);
                            

                        }

                        }
                    }
                    
                    System.out.println(request);

                    String reply = "";
                    if ( task != ""){

                        //within this block we need to check what task is needed 
//===========================================================================================================================
                        //add case
                        if(task.equals("add")){

                            if(!input.equals("") && !input.equals(" "))
                            {
                                //here we do addition to arr
                                arr.add(input);
                                reply = "<!DOCTYPE html><html><body><h2>HTML Forms</h2><form action='http://www.localhost:11111/'><label for='l1'>Task:</label><br><input type='text' id='ftask' name='ftask' value=''><br><label for='l2'>Input:</label><br><input type='text' id='finput' name='finput' value=''><br><br><input type='submit' value='Submit'></form><p>Item added!!!</p></body></html>";

                            }
                            else{
                                //input is empty , error
                                reply ="<!DOCTYPE html><html><body><h2>HTML Forms</h2><form action='http://www.localhost:11111/'><label for='l1'>Task:</label><br><input type='text' id='ftask' name='ftask' value=''><br><label for='l2'>Input:</label><br><input type='text' id='finput' name='finput' value=''><br><br><input type='submit' value='Submit'></form><p>Input is not valid, Please try again</p></body></html>";
                            }


                        }

//======================================================================================================================
                        //rmv case
                        if(task.equals("rmv"))
                        {
                            if(!input.equals("") && !input.equals(" ")){
                                //here i need to remove from the array


                            }
                            else{

                            }


                        }

                        
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
