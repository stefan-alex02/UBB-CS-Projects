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
 * 3 : indicates that a previous message was received successfully (the next value doesn't matter)
 * 4 : indicates that there was an error while receiving the message
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

int try_send_package(int c, struct package aPackage, struct sockaddr * destinationPtr, socklen_t * lPtr) {
    sendto(c, &aPackage, sizeof(struct package), 0,
           destinationPtr, *lPtr);

    struct package response;

    if (recvfrom(c, &response, 10, 0,destinationPtr, lPtr) < 0) {
        error("Error while receiving response from server.\n");
    }

    switch (response.code) {    // Response handling
        case 3:
            if (aPackage.code == 1) {
                printf("Couldn't string length '%hu', retrying...\n", aPackage.message);
            }
            else {
                printf("Couldn't send char '%c', retrying...\n", (char)aPackage.message);
            }
            return 1 + try_send_package(c, aPackage, destinationPtr, lPtr);
        case 4:
            return 0;
        default:
            error("Unknown response received.\n");
            return 0;
    }
}

int main(int argc, char** argv) {
    if (argc != 3) {
        printf("Invalid number of arguments.\nUsage: %s [IP_ADDRESS] [PORT]", argv[0]);
        exit(EXIT_FAILURE);
    }

    int c;
    struct sockaddr_in server;
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

    try_send_package(c, lengthPackage, (struct sockaddr *) &server, &server_len);

    for (int i = 0; i < lengthPackage.message; i++) {
        charPackage.message = (uint16_t)input[i];
        try_send_package(c, charPackage, (struct sockaddr *) &server, &server_len);
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
            reversed[i] = (char) aPackage.message;
            sendto(c, "ACK", 4, 0,
                   (struct sockaddr *) &server, sizeof(server));
        }
    }

    reversed[lengthPackage.message] = '\0';
    printf("Received the following string :\n%s", reversed);

    close(c);
}