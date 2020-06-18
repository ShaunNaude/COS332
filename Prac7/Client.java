import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Client {

    static final String POP3_HOST = "pop.gmail.com";
    static final String POP3_USERNAME = "danienel21";
    static final String POP3_PASSWORD = "erweeenronel";
    static final int POP3_PORT = 995;

    public static String getSender(String[] array){

        String temp = null;
        String sender = null;
        int indexS =0;
        int indexE =0;

        for (int i = 0 ; i < array.length;i++){
            if(array[i].startsWith("From")){
                temp = array[i];
                for (int j = 0 ; j < temp.length();j++){
                    if(temp.charAt(j) == '<'){
                        indexS = j+1;
                    }
                    if(temp.charAt(j)=='>'){
                        indexE = j;
                    }
                }
                sender = temp.substring(indexS,indexE);
            }




        }
        return sender;
    }

    public static String getSubject(String[] array){
        String temp = null;
        String subject = null;
        for (int i = 0 ; i < array.length;i++){
            if(array[i].startsWith("Subject")){
                temp = array[i];
                subject = temp.substring(9);

            }
        }
        return subject;
    }
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread();

        while(true){
            POP3listener POP3client = new POP3listener(POP3_HOST,POP3_USERNAME,POP3_PASSWORD,POP3_PORT);

            try  {

                System.out.println("Connecting to POP3 server...");
                POP3client.connectAndAuthenticate();
                System.out.println("Connected to POP3 server.");
                int messageCount = POP3client.getMessageCount();
                System.out.println("\nWaiting massages on POP3 server : " + messageCount);
                String[] messages = POP3client.getHeaders();

                for (int i=0; i<messages.length; i++) {

                    StringTokenizer messageTokens = new StringTokenizer(messages[i]);
                    String messageId = messageTokens.nextToken();
                    String messageSize = messageTokens.nextToken();
                    String[] messageBody = POP3client.getMessageHead(messageId,1);
                    System.out.println("/////////////////////////////////////////////////////////");
                    System.out.println("MESSAGE : "+messageId);
                    System.out.println("/////////////////////////////////////////////////////////");

                    System.out.println(getSender(messageBody));
                    for (int j = 0 ; j < messageBody.length ; j++){
                        System.out.println(messageBody[j]);
                    }



                    if(getSubject(messageBody).equals("prac7")){
                        ArrayList<String> emails = new ArrayList<String>();
                        File file = new File("email.txt");
                        Scanner sc = new Scanner(file);
                        while (sc.hasNextLine()){
                            emails.add(sc.nextLine());
                        }
                        String sender = getSender(messageBody);
                        if(!emails.contains(sender)){
                            System.out.println("Doesnt contain email");
                            try {
                                BufferedWriter out = new BufferedWriter(
                                        new FileWriter("email.txt", true));
                                out.write(sender);
                                out.close();
                            }
                            catch (IOException e) {
                                System.out.println("exception occoured" + e);
                            }
                        }else{
                            System.out.println("Already emailed");
                        }
                    }
                    System.out.println("/////////////////////////////////////////////////////////");
                    System.out.println("/////////////////////////////////////////////////////////");
                }
            } catch (Exception e) {
                POP3client.close();
                System.out.println("Can not receive e-mail!");
                e.printStackTrace();
            }

            thread.sleep(10000);
        }



    }

}
