#include <stdio.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <string.h>

#define PORT 8080

int main(int argc, char const *argv[]){

    int new_socket;
    long valread;
    
    struct sockaddr_in address;
    int addrlen = sizeof(address);

    int server_fd = socket(AF_INET,SOCK_STREAM,0);
    if(server_fd == 0){
        perror("Socket failure");
        exit(EXIT_FAILURE);
        
    }

    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons( PORT );

    memset(address.sin_zero,'/0',sizeof(address.sin_zero));

    if(bind(server_fd,(struct sockadrr *)&address,sizeof(address))<0){
        perror("Socket failed to bind");
        exit(EXIT_FAILURE);

    }
    if(listen(server_fd,10)< 0){
        perror("No connections");
        exit(EXIT_FAILURE);

    } 

    char *response;
    while(1){
        printf("\n///////////////////////////////////////////\n");
        printf("/////   WAITING FOR NEW CONNECTION    /////\n");
        printf("///////////////////////////////////////////\n\n");

        if((new_socket = accept(server_fd,(struct sockaddr *)&address,(socklen_t*)&addrlen))<0){
            perror("In accept");
            exit(EXIT_FAILURE);
            
        }


        char buffer[30000] = {0};
        valread= read(new_socket, buffer, 30000);
        printf("%s\n",buffer);
        write(new_socket,response,strlen(response));
       
        printf("%s\n", buffer);


        printf("\n///////////////////////////////////////////\n");
        printf("/////   HELLO MESSAGE SENT            /////\n");
        printf("///////////////////////////////////////////\n\n");

        close(new_socket);
        
    }



    return 0;
}