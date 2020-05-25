import java.io.*;
import java.net.*;
import java.util.*;

public class webserver_OG extends Thread {
    public static class questionObj{
            public String text;
            public ArrayList<answerObj> answers = new ArrayList<answerObj>();
        }
        public static class answerObj{
            public String text;
            public boolean status;
        }
        
        public static ArrayList<questionObj> questions = new ArrayList<questionObj>();
        
        public static void main(String argv[]) throws Exception
        {
            //Reading questions
            BufferedReader fileReader = null;
            fileReader = new BufferedReader(new FileReader("test.txt"));
            
            String sCurrentLine;
            int correct = 0;
            int wrong = 0;

            questionObj question = null;
            while ((sCurrentLine = fileReader.readLine()) != null) {
		if (sCurrentLine.charAt(0) == '?'){
                    if (question != null){
                        if (correct == 0){
                            answerObj answer = new answerObj();
                            answer.text = "None of the above";
                            answer.status = true;
                            question.answers.add(answer);
                        } else if (correct > 1){
                            answerObj answer = new answerObj();
                            answer.text = "More than one of the above";
                            answer.status = true;
                            question.answers.add(answer);
                        }  
                        questions.add(question);
                        question = null;
                    }
                    
                    correct = 0;
                    wrong = 0;
                    question = new questionObj();
                    question.text = sCurrentLine.substring(1);
                } else if (sCurrentLine.charAt(0) == '-'){
                    answerObj answer = new answerObj();
                    answer.text = sCurrentLine.substring(1);
                    answer.status = false;
                    question.answers.add(answer);
                        
                    wrong++;
                } else if (sCurrentLine.charAt(0) == '+'){
                    answerObj answer = new answerObj();
                    answer.text = sCurrentLine.substring(1);
                    answer.status = true;
                    question.answers.add(answer);
                    
                    correct++;
                }
            }
            
            if (question != null){
                if (correct == 0){
                    answerObj answer = new answerObj();
                    answer.text = "None of the above";
                    answer.status = true;
                    question.answers.add(answer);
                } else if (correct > 1){
                    answerObj answer = new answerObj();
                    answer.text = "More than one of the above";
                    answer.status = true;
                    question.answers.add(answer);
                }  
                questions.add(question);
                question = null;
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
                    Random rand = new Random();
                    
                    //Get request from client
                    int length = -1;
                    while ((line = in.readLine()) != null && (line.length() != 0)) {
                        //System.out.println(line);
                        if (line.indexOf("Content-Length:") > -1) {
                            length = new Integer(line.substring(line.indexOf("Content-Length:") + 16, line.length())).intValue();
                        }
                    }
                    String request = "";
                    for (int i = 0; i < length; i++) {
                        int input = in.read();
                        request += (char)input;
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
                        
                        int n = rand.nextInt(questions.size());
                
                        //generate page
                        reply = "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>COS 332 Quiz</title></head><body><form method='POST' action='' ><h1>COS 332 Browser quiz</h1>";
                        reply += "<h2>" + questions.get(n).text + "</h2>";    

                         for (int i = 0; i < questions.get(n).answers.size(); i++)
                        {
                            reply += "<input type='radio' name='answer' value='" + questions.get(n).answers.get(i).status + "'> " + questions.get(n).answers.get(i).text + " </input> <br>";
                        }

                        reply += "<input type='submit' value='submit'></form></body></html>";
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
