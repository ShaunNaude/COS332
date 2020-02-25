#include <iostream>
#include <sys/types.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <string.h>
#include <string>

using namespace std;

int main() {

    /*
    *we can put all this code into classes to make it neater once we know whats cracking.
    * for example we can put all the socket info in a socket struct 
    */



    //Create socket
    int Listening = socket(AF_INET,SOCK_STREAM,0);

    if  (Listening == -1 )
    {
        cout<<"Socket cannot be created";
        return 0;
    }

    //Now we going to bind the socket to an IP/PORT
    sockaddr_in mySocketInfo;
    mySocketInfo.sin_family = AF_INET; // this tells the socket to use ipV4
    mySocketInfo.sin_port = htons(55555); // this tells the socket which port to use *htons() this function changes between little/big endian
    inet_pton(AF_INET, "0.0.0.0", &mySocketInfo.sin_addr); // here we tell the socket to use any IP address on the system
    
    bind(Listening, (sockaddr*)&mySocketInfo, sizeof(mySocketInfo));

    //start listening , SOMAXCONN=127 connections
    listen(Listening, SOMAXCONN);


    //setting up the client / accept call

    // Wait for a connection
    sockaddr_in client;
    socklen_t clientSize = sizeof(client);
 
    int clientSocket = accept(Listening, (sockaddr*)&client, &clientSize);
 
    char host[NI_MAXHOST];      // Client's remote name
    char service[NI_MAXSERV];   // Service (i.e. port) the client is connect on

     memset(host, 0, NI_MAXHOST); // same as memset(host, 0, NI_MAXHOST);
     memset(service, 0, NI_MAXSERV);

     if (getnameinfo((sockaddr*)&client, sizeof(client), host, NI_MAXHOST, service, NI_MAXSERV, 0) == 0)
    {
        cout << host << " connected on port " << service << endl;
    }
    else
    {
        inet_ntop(AF_INET, &client.sin_addr, host, NI_MAXHOST);
        cout << host << " connected on port " << ntohs(client.sin_port) << endl;
    }
 
    // Close listening socket
    close(Listening);
 
    // While loop: accept and echo message back to client
    char buf[4096];
 
    while (true)
    {
        memset(buf, 0, 4096);
 
        // Wait for client to send data
        int bytesReceived = recv(clientSocket, buf, 4096, 0);
        if (bytesReceived == -1)
        {
            cerr << "Error in recv(). Quitting" << endl;
            break;
        }
 
        if (bytesReceived == 0)
        {
            cout << "Client disconnected " << endl;
            break;
        }
 
        cout << string(buf, 0, bytesReceived) << endl;
 
        // Echo message back to client
        send(clientSocket, buf, bytesReceived + 1, 0);
    }
 
    // Close the socket
    close(clientSocket);
 
    return 0;

 

}