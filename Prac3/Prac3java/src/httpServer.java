import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class httpServer implements Runnable {

    //PORT OF THE SERVER
    static final int PORT = 8080;

    static final File webRoot = new File(".");
    static final String index = "index.html";
    static final String error404 = "404.html";
    static final String notImplemented = "notImplemented.html";
    // enables logs
    static final boolean logs = true;
    //socket
    private Socket conSocket;
     private boolean serverRunning;

    //Constructor
    public httpServer (Socket clientSocket){
        serverRunning = true;
        conSocket = clientSocket;

    }
    public static void main(String[] args){
        try{
            ServerSocket conServer = new ServerSocket(PORT);
            System.out.println("Server Started on port :"+PORT);
            System.out.print("Now waiting for connections");

            while (true){
                httpServer server = new httpServer(conServer.accept());
                if(logs){
                    System.out.println("Client connected at : "+ new Date());
                }
                Thread serverThread = new Thread(server);
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String requestedHTML;
        String inputClient;
        BufferedReader inputBufferedReader = null;
        PrintWriter outputPrintWriter = null;
        BufferedOutputStream outputBufferedOutputStream = null;
        StringTokenizer parseInput;
        String httpMethod;
        File htmlFile;

        try{
            //Read input from the socket that the client connect to into a buffer.
            inputBufferedReader = new BufferedReader(new InputStreamReader(conSocket.getInputStream()));
            outputPrintWriter = new PrintWriter(conSocket.getOutputStream());
            outputBufferedOutputStream = new BufferedOutputStream(conSocket.getOutputStream());
            //get the data from the inputBuffer
            inputClient = inputBufferedReader.readLine();

            parseInput = new StringTokenizer(inputClient);
            httpMethod = parseInput.nextToken().toUpperCase();
            requestedHTML = parseInput.nextToken().toLowerCase();

            if(!httpMethod.equals("GET")&&!httpMethod.equals("HEAD")){
                if (logs){
                    System.out.println("501 Not implemented : "+ httpMethod+" method");
                }
                htmlFile = new File(webRoot, notImplemented);
                int length = (int)htmlFile.length();
                byte[] fileData = readFileData(htmlFile,length);

                //Building the http header if a method was not implemented.

                outputPrintWriter.println("HTTP/1.1 501 Not Implemented");
                outputPrintWriter.println("Server: httpServer Java COS 332 Prac 3 (ArchLinux)");
                outputPrintWriter.println("Date: "+ new Date());
                outputPrintWriter.println("Content Length: " +length);
                outputPrintWriter.println("Content-type: text/html" );
                outputPrintWriter.println("");
                outputPrintWriter.flush();

                outputBufferedOutputStream.write(fileData ,0,length);
                outputBufferedOutputStream.flush();


            }else{ // GET AND HEAD method that we support
                if(requestedHTML.endsWith("/")){
                    requestedHTML+= index;
                }
                htmlFile = new File(webRoot,requestedHTML);
                int length = (int) htmlFile.length();
                byte[] fileData = readFileData(htmlFile,length);

                outputPrintWriter.println("HTTP/1.1 200 OK");
                outputPrintWriter.println("Server: httpServer Java COS 332 Prac 3 (ArchLinux)");
                outputPrintWriter.println("Date: "+new Date());
                outputPrintWriter.println("Content-length: "+length);
                if(requestedHTML.endsWith("js")){
                    outputPrintWriter.println("Content-type: application/javascript");
                }else{
                    outputPrintWriter.println("Content-type: text/html");
                }

                outputPrintWriter.println("");
                outputPrintWriter.flush();

                outputBufferedOutputStream.write(fileData,0,length);
                outputBufferedOutputStream.flush();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputBufferedReader.close();
                outputPrintWriter.close();
                outputBufferedOutputStream.close();
                conSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private byte[] readFileData(File htmlFile, int length) throws IOException {
        FileInputStream fileInput = null;
        byte[] fileData = new byte[length];
        try{
            fileInput = new FileInputStream(htmlFile);
            fileInput.read(fileData);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileInput!=null){
                fileInput.close();
            }
        }
        return fileData;
    }
}
