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

struct Package {
    uint16_t code;
    uint16_t length;
    char character;
};

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

    struct Package lengthPackage, charPackage;
    lengthPackage.code = 1;
    lengthPackage.length = strlen(input);
    charPackage.code = 2;

    sendto(c, &lengthPackage, sizeof(struct Package), 0,
           (struct sockaddr *) &server, sizeof(server));

    char buffer[10];

    for (int i = 0; i < lengthPackage.length; i++) {
        charPackage.character = input[i];
        sendto(c, &charPackage, sizeof(struct Package), 0,
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

    struct Package package;
    for (int i = 0; i < lengthPackage.length; i++) {
        if (recvfrom(c, &package, sizeof(struct Package), MSG_WAITALL,
                     (struct sockaddr *) &server, &server_len) < 0) {
            error("Error while receiving response from server.\n");
        }
        else if (package.code != 2){
            sendto(c, "RTY", 4, 0,
                   (struct sockaddr *) &server, sizeof(server));
            i--;
        }
        else {
            reversed[i] = package.character;
            sendto(c, "ACK", 4, 0,
                   (struct sockaddr *) &server, sizeof(server));
        }
    }

    reversed[lengthPackage.length] = '\0';
    printf("Received the following string :\n%s", reversed);

    close(c);
}