#include <stdio.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <unistd.h>
#include <netinet/in.h>
#include <string.h>
#include <arpa/inet.h>

#define PORT 8080

int main(int argc, char const *argv[]){

    struct sockaddr_in serv_addr;


    char *hello = "Hello form client";
    char buffer[1024]={0};


    long valread;


    int sock = socket(AF_INET,SOCK_STREAM,0);
    if(sock < 0 ){
        printf("Socket init  error");
        return -1;
    }

    memset(&serv_addr,'0',sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(PORT);

    if(inet_pton(AF_INET,"127.0.0.1",&serv_addr.sin_addr)<=0){
        printf("\nInvalid address\n");
        return -1;
    }

    if(connect(sock,(struct sockaddr *)&serv_addr,sizeof(serv_addr))<0){
        printf("\n Connection Failure");
        return -1;
    }   
    send(sock,hello,strlen(hello),0);
    printf("Hello message sent \n");
    valread = read(sock, buffer ,1024);
    printf("%s\n",buffer );
    return 0;
    
}
