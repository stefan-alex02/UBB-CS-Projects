/* clientUDP.c (Var 1)
 * Un client trimite unui server un sir de lungime cel mult 100 de caractere.
 * Serverul va returna clientului acest sir oglindit (caracterele sirului in ordine inversa).
 */
/*
 * Client-Server sending protocol :
 * Package structs of format : [message code : 2 bytes] [message]
 * Message code legend :
 * 1 : indicates the message contains the length of the string -> 2 bytes containing the string length
 * 2 : indicates the message contains the new port to which the client should connect to -> 2 bytes
 * 3 : indicates the message contains a character of the string -> 1 byte containing a string char
 * 4 : indicates that a previous message was received successfully
 */

#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <arpa/inet.h>

#define MAX_BUFFER_SIZE 1001

void error(char *s) {
    perror(s);
    exit(EXIT_FAILURE);
}

struct Package {
    uint16_t code;
    union {
        uint16_t length;
        uint16_t newPort;
        char character;
    };
};

int get_port(char* port) {
    if (port == NULL) {
        error("Error: port is empty.\n");
    }

    char* str_part = NULL;
    long int_part = strtol(port, &str_part, 10);

    if (strlen(str_part) != 0 || (port[0] != '0' && int_part == 0)) {
        error("Error: port is invalid.\n");
    }
    if (int_part < 0 || int_part > 65535) {
        error("Error: port must be a number between [0, 65535].\n");
    }
    return (int)int_part;
}

int create_socket() {
    int c = socket(AF_INET, SOCK_DGRAM, 0);
    if (c < 0) {
        error("Error while creating server socket.\n");
    }
    return c;
}

int main(int argc, char** argv) {
    if (argc != 3) {
        printf("Invalid number of arguments.\nUsage: %s [IP_ADDRESS] [PORT]", argv[0]);
        exit(EXIT_FAILURE);
    }

    int c = create_socket();

    struct sockaddr_in server;
    socklen_t sl = sizeof(server);
    memset(&server, 0, sl);
    server.sin_port = htons(get_port(argv[2]));
    server.sin_family = AF_INET;
    if (inet_aton(argv[1], &server.sin_addr) == 0) {
        error("Error: IP address is invalid.\n");
    }

    printf("Type input string (max. 100 chars) :\n> ");

    char input[MAX_BUFFER_SIZE];
    if (fgets(input, MAX_BUFFER_SIZE - 1, stdin) == NULL) {
        error("Error while reading input.\n");
    }
    input[strlen(input) - 1] = '\0';

    struct Package lengthPackage;
    lengthPackage.code = htons(1);                 // Length type of message
    lengthPackage.length = htons(strlen(input));

    sendto(c, &lengthPackage, sizeof(lengthPackage), 0,
           (struct sockaddr *) &server, sl);

    lengthPackage.length = ntohs(lengthPackage.length);

    // Receiving the new port from the server to connect to
    struct Package portPackage;

    if (recvfrom(c, &portPackage, sizeof(portPackage), MSG_WAITALL,
                 (struct sockaddr *) &server, &sl) < 0) {
        error("Error while receiving first package from client.\n");
    }
    portPackage.code = ntohs(portPackage.code);
    portPackage.newPort = ntohs(portPackage.newPort);

    if (portPackage.code != 2) {
        error("Received the wrong package.\n");
    }

    printf("Establishing new private connection on port %d.\n", portPackage.newPort);

    struct sockaddr_in newServer;
    unsigned newSl = sizeof(newServer);
    memset(&newServer, 0, newSl);
    newServer.sin_port = htons(portPackage.newPort);
    newServer.sin_family = AF_INET;
    newServer.sin_addr.s_addr = server.sin_addr.s_addr;

    struct Package charPackage;
    charPackage.code = htons(3);                   // Char type of message

    printf("Press ENTER key to continue");
    getchar();

    // Setting up the confirmation package
    struct Package confirmPackage;

    // Sending the packages to the new server
    for (int i = 0; i < lengthPackage.length; i++) {
        charPackage.character = input[i];

        // Sending the character package
        sendto(c, &charPackage, sizeof(charPackage), 0,
               (struct sockaddr *) &newServer, newSl);

        // ----------------------------------------
        // - No other operations are allowed here -
        // ----------------------------------------

        // Waiting for confirmation
        if (recvfrom(c, &confirmPackage, sizeof(confirmPackage), MSG_WAITALL,
                     (struct sockaddr *) &newServer, &newSl) < 0) {
            error("Error while receiving confirmation from new server.\n");
        }
        confirmPackage.code = ntohs(confirmPackage.code);
        if (confirmPackage.code != 4) {
            error("Couldn't receive confirmation from new server.\n");
        }

//        printf("Confirmation of sent package received. (i = %d)\n", i + 1);
    }

    char reversed[MAX_BUFFER_SIZE];
    memset(reversed, 0, lengthPackage.length);

    confirmPackage.code = htons(4);

    // Sending the reversed packages from the new server
    for (int i = 0; i < lengthPackage.length; i++) {
        // Waiting for character package
        if (recvfrom(c, &charPackage, sizeof(charPackage), MSG_WAITALL,
                     (struct sockaddr *) &newServer, &newSl) < 0) {
            error("Error while receiving package from server.\n");
        }
        charPackage.code = ntohs(charPackage.code);
        if (charPackage.code != 3) {
            error("Received wrong package from new server.\n");
        }
        reversed[i] = charPackage.character;

//        printf("Package received successfully. (i = %d)\n", i + 1);

        // Sending the confirmation package
        sendto(c, &confirmPackage, sizeof(confirmPackage), 0,
               (struct sockaddr *) &newServer, newSl);
    }

    reversed[lengthPackage.length] = '\0';
    printf("Received the following string :\n%s\n", reversed);

    close(c);
}