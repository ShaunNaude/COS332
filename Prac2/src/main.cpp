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



}