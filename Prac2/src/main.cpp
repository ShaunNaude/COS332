#include <iostream>
#include <sys/types.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <string.h>
#include <string>
#include <fstream>
#include <bits/stdc++.h> 

using namespace std;

int main() {

    /*
    *we can put all this code into classes to make it neater once we know whats cracking.
    * for example we can put all the socket info in a socket struct 
    */

    vector<string> appointments;

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

    //==============read appointments from txt file to vector============================

        string line;
        ifstream myfile;
        myfile.open("database.txt");
        if( myfile.is_open() )
        {
            while( getline( myfile , line ) ){
                
                appointments.push_back(line) ;
            }
        }

        myfile.close();

    //===================================================================================
 
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
 
//===============================================================================================================
    // Above is the code that makes the server work
//===============================================================================================================
    // While loop: accept and echo message back to client
    char buf[4096];
    

   string Prompt = "P\> ";
   string OutputStr;
   string item;
   bool found = false;
   
   
   
 
    while (true)
    {

//=========================================================================================================
        memset(buf, 0, 4096);
        found = false;

        send(clientSocket,Prompt.c_str(),sizeof(Prompt.c_str()),0);
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
//============== write appointments to txt file===============================================
            ofstream writeFile ("database.txt");
            for(int i = 0; i<appointments.size(); i++)
                {
                    writeFile<<appointments[i]<<endl;
                }

                writeFile.close();

//============================================================================================
            break;
        }

/*
=========================================================================================================
    here we add the functions for our server
    ------------------------------------------

    1) add everything to vector then write vector to txt on disconnect
    2) Note all functions must be 3 characters long, cause we only going to test the first 3 bytes of the buf.
=========================================================================================================
*/
    //==============Add appointment to calendar===========================================
        if(string(buf, 0, 3) == "add")
        {
            item = string(buf,4,bytesReceived-6);
            appointments.push_back(item);
            OutputStr = "|| " + item + " ||" + " >> was added!!!"+string(buf,bytesReceived-2,bytesReceived);
            send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);

            //reset variables
            item = "";
            OutputStr = "";

        }    
    //=====================================================================================
    //================list current appointments============================================
        if( string(buf,0,3) == "lst" )
        {
            OutputStr = "All Events" + string(buf,bytesReceived-2,bytesReceived) ;
            send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);
            OutputStr = "===========================" + string(buf,bytesReceived-2,bytesReceived);
            send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);
            OutputStr = "";
            send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);

            for(int i=0 ; i < appointments.size() ; i++ )
            {                                   
                OutputStr = OutputStr + appointments[i] + string(buf,bytesReceived-2,bytesReceived) ;
            }

            send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);
            OutputStr = "===========================" + string(buf,bytesReceived-2,bytesReceived);
            send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);

            //reset variable
            OutputStr = "";

        }

    //=====================================================================================

    //=========================remove item form appointments===============================
        if( string(buf,0,3) == "rmv" )
        {
            found = false;
            string item = string(buf,4,bytesReceived-6);
            for(int i = 0; i < appointments.size(); i++)
            {
                if( appointments[i] == item )
                {
                    appointments.erase(appointments.begin() + i);
                    
                    OutputStr = "|| " + item + " ||" + " >> was removed!!!"+string(buf,bytesReceived-2,bytesReceived);
                    send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);
                    found = true;
                    
                }
            }

                if( found == false)
                {
                    OutputStr = "|| " + item + " ||" + " >> Could not be found!!!"+string(buf,bytesReceived-2,bytesReceived);
                    send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);
                }
        // reset variables
            item = "";
            OutputStr = "";
            found = false;
        }



    //=====================================================================================

    //==========================search for an Item ========================================
        if( string(buf,0,3) == "fnd" )
        {
            string item = string(buf,4,bytesReceived-6);

             for(int i = 0; i < appointments.size(); i++)
            {
                if( appointments[i] == item )
                {
                    OutputStr = "|| " + item + " ||" + " >> was found!!!"+string(buf,bytesReceived-2,bytesReceived);
                    send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);
                    found = true;
                    
                }
            }

            if( found == false)
                {
                    OutputStr = "|| " + item + " ||" + " >> Could not be found!!!"+string(buf,bytesReceived-2,bytesReceived);
                    send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);
                }


            //reset variables
            OutputStr = "";
        }

    //=====================================================================================

    //=========================Help function================================================
    //
         if( string(buf,0,3) == "hlp" ){

             OutputStr = "All functions" + string(buf,bytesReceived-2,bytesReceived) ;
             send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);
             OutputStr = "=================================="+string(buf,bytesReceived-2,bytesReceived);
             send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);

             OutputStr = "1) add 'name of appointment'  << this adds to your appointments"+string(buf,bytesReceived-2,bytesReceived);
             send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);

             OutputStr = "2) rmv 'name of appointment' << this removes the appointment if it is found"+string(buf,bytesReceived-2,bytesReceived);
             send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);

             OutputStr = "3) fnd 'name of appointment' << this searches if the appointment is in your database"+string(buf,bytesReceived-2,bytesReceived);
             send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);

             OutputStr = "4) lst  << this lists all active appointments"+string(buf,bytesReceived-2,bytesReceived);
             send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);

            //reset variables
            OutputStr = "";

         }

            string check = string(buf,0,3);
         if(string(buf,0,3) != "hlp" && string(buf,0,3) != "add" && string(buf,0,3) != "rmv" && string(buf,0,3) != "lst" && string(buf,0,3) != "fnd" )
            {
                OutputStr = "Unrecognized Command! Use 'hlp' for Command list " + string(buf,bytesReceived-2,bytesReceived);
                send(clientSocket,OutputStr.c_str(),OutputStr.length(),0);
            }


    //======================================================================================



//===============================================================================================================
        
 
        
        //this code makes a new line, not optimal but it works
         memset(buf,0,bytesReceived-2);
         send(clientSocket, buf, bytesReceived + 1, 0);
    }
 
//===============================================================================================================

//===============================================================================================================


    // Close the socket
    close(clientSocket);
 
    return 0;

 

}