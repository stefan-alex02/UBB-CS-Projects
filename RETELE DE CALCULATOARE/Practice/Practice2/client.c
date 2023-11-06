/*
 * Practice 2 : Boltz game using UDP
 * Client sends 'PLAY' string, server (concurrently) receives it, sends back the
 * new port to which the client should connect to, then starts playing (a number
 * containing or divisible by 7 will randomly be displayed as 'Boltz' or as the number
 * itself, causing the game to end).
 */

#include <stdio.h>
#include <stdlib.h>
#include <bits/stdint-uintn.h>
#include <string.h>
#include <errno.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <time.h>

int isBoltz(unsigned n) {
    if (n % 7 == 0) {
        return 1;
    }
    while(n > 0) {
        if (n % 10 == 7)
            return 1;
        n /= 10;
    }
    return 0;
}

void error(char* message) {
    if (errno != 0) {
        perror(message);
    }
    else {
        fprintf(stderr, "%s", message);
    }
    exit(EXIT_FAILURE);
}

uint16_t get_port(char* portStr) {
    long port;
    char* text = NULL;
    if ((port = strtol(portStr, &text, 10)) == 0 && portStr[0] != '0' ||
        strlen(text) > 0 ||
        !(port >= 0 && port <= 65535)) {
        error("Port is invalid.\n");
    }
    return (uint16_t)port;
}

in_addr_t get_ip_address(char *string) {
    struct in_addr address;
    if (inet_aton(string, &address) == 0) {
        error("IP ADDRESS is invalid.\n");
    }
    return address.s_addr;
}

int main(int argc, char** argv) {
    srand(time(NULL));

    // Checking arguments and validating them
    if (argc != 3) {
        printf("Error: invalid number of arguments.\nUsage: %s [IP ADDRESS] [PORT]\n", argv[0]);
        exit(EXIT_FAILURE);
    }
    uint16_t port = get_port(argv[2]);
    in_addr_t ipAddress = get_ip_address(argv[1]);

    // Opening the socket
    int c = socket(AF_INET, SOCK_DGRAM, 0);
    if (c < 0) {
        error("Error while creating socket.\n");
    }

    // The main server struct
    struct sockaddr_in server;
    unsigned l = sizeof(server);
    memset(&server, 0, l);
    server.sin_family = AF_INET;
    server.sin_port = htons(port);
    server.sin_addr.s_addr = ipAddress;

    // The new port of the new server
    uint16_t newPort = 0;

    // Send the PLAY message
    if (sendto(c, "PLAY", 4, 0, (struct sockaddr *) &server, l) < 0) {
        error("Error while trying to send PLAY message.\n");
    }

    // Receiving the new port to connect to
    if (recvfrom(c, &newPort, sizeof(uint16_t), 0, (struct sockaddr *) &server, &l) < 0) {
        error("Error while trying to receive NEW PORT.\n");
    }

    // The new server struct
    struct sockaddr_in newServer;
    unsigned newL = sizeof(newServer);
    memset(&newServer, 0, newL);
    newServer.sin_family = AF_INET;
    newServer.sin_port = newPort;
    newServer.sin_addr.s_addr = ipAddress;

    // Waiting for prompt
    printf("Begin?\n");
    getchar();

    // Starting the game
    uint32_t number = 1;
    uint32_t response;
    while(1) {
        // Timeout
        sleep(1);

        // Sending the number, depending on its value and randomness
        if (isBoltz(number) && rand() % 4 > 0) {
            printf("Sending : BOLTZ\n");
            char* boltzMsg = "BTZ";
            if (sendto(c, boltzMsg, 4, 0, (struct sockaddr *) &newServer, newL) < 0) {
                error("Error while trying to send BOLTZ.\n");
            }
        }
        else {
            printf("Sending : %d\n", number);
            number = htonl(number);
            if (sendto(c, &number, sizeof(uint32_t), 0, (struct sockaddr *) &newServer, newL) < 0) {
                error("Error while trying to send NUMBER.\n");
            }
            number = ntohl(number);
        }

        // Receiving the new value from the server
        if (recvfrom(c, &response, sizeof(uint32_t), 0, (struct sockaddr *) &newServer, &newL) < 0) {
            error("Error while trying to receive NUMBER.\n");
        }

        // If it's a number or boltz, ignore the response,
        // else (if it's FAIL message) then the game is lost
        if (strcmp((char*)&response, "FAL") == 0) {
            printf("Ouch! I lost.\n");
            break;
        }
        else {
            if (strcmp((char*)&response, "BTZ") == 0) {
                printf("Got from server : BOLTZ\n");
            }
            else {
                response = ntohl(response);
                printf("Got from server : %d\n", response);
                if (isBoltz(response)) {
                    printf("Server is wrong! I won.\n");

                    char* failMsg = "FAL";
                    if (sendto(c, failMsg, 4, 0, (struct sockaddr *) &newServer, newL) < 0) {
                        error("Error while trying to send FAIL message.\n");
                    }
                    break;
                }
            }
        }
        number += 2;
    }

    close(c);
}
