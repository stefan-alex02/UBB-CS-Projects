/* clientUDP.c (Var 1)
 * Un client trimite unui server un sir de lungime cel mult 100 de caractere.
 * Serverul va returna clientului acest sir oglindit (caracterele sirului in ordine inversa).
 */
/*
 * Client-Server sending protocol :
 * Package structs of format : [message code : 2 bytes] [message]
 * Message code legend :
 * 1 : indicates the message contains the length of the string -> 2 bytes containing the string length
 * 2 : indicates the message contains a character of the string -> 1 byte containing a string char
 */

#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <arpa/inet.h>

#define MAX_BUFFER_SIZE 101

void error(char *s) {
    perror(s);
    exit(EXIT_FAILURE);
}

struct package {
    uint16_t code;
    uint16_t message;
};

int try_send_package(int c, struct sockaddr_in destination, struct package aPackage) {

}

int main(int argc, char** argv) {
    if (argc != 3) {
        printf("Invalid number of arguments.\nUsage: %s [IP_ADDRESS] [PORT]", argv[0]);
        exit(EXIT_FAILURE);
    }

    int c;
    struct sockaddr_in server, client;
    socklen_t server_len = sizeof(server);

    c = socket(AF_INET, SOCK_DGRAM, 0);
    if (c < 0) {
        error("Error while creating client socket.\n");\
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(atoi(argv[2]));
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr(argv[1]);

    printf("Type input string (max. 100 chars) :\n> ");

    char input[MAX_BUFFER_SIZE];
    if (fgets(input, MAX_BUFFER_SIZE - 1, stdin) == NULL) {
        error("Error while reading input.\n");
    }
    input[strlen(input) - 1] = '\0';

    struct package lengthPackage, charPackage;
    lengthPackage.code = 1;                     // Length type of message
    lengthPackage.message = strlen(input);
    charPackage.code = 2;                       // Char type of message

    sendto(c, &lengthPackage, sizeof(struct package), 0,
           (struct sockaddr *) &server, sizeof(server));

    char buffer[10];

    for (int i = 0; i < lengthPackage.message; i++) {
        charPackage.message = (uint16_t)input[i];
        sendto(c, &charPackage, sizeof(struct package), 0,
                (struct sockaddr *) &server, sizeof(server));

        if (recvfrom(c, buffer, 10, 0,
                     (struct sockaddr *)&server, &server_len) < 0) {
            error("Error while receiving response from server.\n");
        }
        else if (strcmp(buffer, "ACK") != 0){
            printf("Couldn't send char '%c', retrying...\n", input[i]);
            i--;
        }
    }

    char reversed[MAX_BUFFER_SIZE];

    struct package aPackage;
    for (int i = 0; i < lengthPackage.message; i++) {
        if (recvfrom(c, &aPackage, sizeof(struct package), MSG_WAITALL,
                     (struct sockaddr *) &server, &server_len) < 0) {
            error("Error while receiving response from server.\n");
        }
        else if (aPackage.code != 2){
            sendto(c, "RTY", 4, 0,
                   (struct sockaddr *) &server, sizeof(server));
            i--;
        }
        else {
            reversed[i] = (char) *(char*)((void*)&aPackage.message - 4);
//            reversed[i] = (char) aPackage.message;
            sendto(c, "ACK", 4, 0,
                   (struct sockaddr *) &server, sizeof(server));
        }
    }

    reversed[lengthPackage.message] = '\0';
    printf("Received the following string :\n%s", reversed);

    close(c);
}