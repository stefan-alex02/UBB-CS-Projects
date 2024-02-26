/*
 * Practice 2 : Boltz game using UDP
 * Server
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
#include <signal.h>
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
        error("[P] Port is invalid.\n");
    }
    return (uint16_t)port;
}
int main(int argc, char** argv) {
    srand(time(NULL));

    // Checking arguments and validating them
    if (argc != 2) {
        printf("[P] Error: invalid number of arguments.\nUsage: %s [PORT]\n", argv[0]);
        exit(EXIT_FAILURE);
    }
    uint16_t port = get_port(argv[1]);

    // Opening the socket
    int s = socket(AF_INET, SOCK_DGRAM, 0);
    if (s < 0) {
        error("[P] Error while creating socket.\n");
    }

    // The server struct
    struct sockaddr_in server;
    unsigned l = sizeof(server);
    memset(&server, 0, l);
    server.sin_family = AF_INET;
    server.sin_port = htons(port);
    server.sin_addr.s_addr = INADDR_ANY;

    // Binding the server
    if (bind(s, (struct sockaddr *) &server, l) < 0) {
        error("[P] Error binding.\n");
    }

    // Counting the clients
    uint16_t clientCount = 0;

    signal(SIGCHLD, SIG_IGN); // Zombie killer

    while(1) {
        // The pid of the latest child server
        int pid;

        // The client struct
        struct sockaddr_in client;
        unsigned cl = sizeof(client);
        memset(&client, 0, cl);

        // The message string
        char message[5];

        // Receiving the PLAY string from the client
        if (recvfrom(s, &message, 4, 0, (struct sockaddr *) &client, &cl) < 0) {
            error("[P] Error while trying to receive PLAY message.\n");
        }
        message[4] = '\0';
        // Checking the message
        if (strcmp(message, "PLAY") != 0) {
            error("[P] Received wrong message from client.\n");
        }

        clientCount++;
        if ((pid = fork()) == 0) {
            // Creating new server socket
            int newS = socket(AF_INET, SOCK_DGRAM, 0);
            if (newS < 0) {
                error("[C] Error while creating socket.\n");
            }

            // New server struct with a new port
            struct sockaddr_in newServer;
            unsigned newL = sizeof(newServer);
            memset(&newServer, 0, newL);
            newServer.sin_family = AF_INET;
            newServer.sin_port = htons(port + clientCount);
            newServer.sin_addr.s_addr = client.sin_addr.s_addr;

            // Binding the new server
            if (bind(newS, (struct sockaddr *) &newServer, newL) < 0) {
                error("[C] Error binding.\n");
            }

            // Sending new port to the client, via the old socket
            if (sendto(s, &newServer.sin_port, sizeof(uint16_t), 0, (struct sockaddr *) &client, cl) < 0) {
                error("[C] Error while trying to send NEW PORT.\n");
            }

            // Starting the game
            uint32_t number = 1;
            uint32_t response;
            while(1) {
                // Receiving the new value from the client
                if (recvfrom(newS, &response, sizeof(uint32_t), 0, (struct sockaddr *) &client, &cl) < 0) {
                    error("[C] Error while trying to receive NUMBER.\n");
                }

                // If it's a number or boltz, ignore the response,
                // else (if it's FAIL message) then the game is lost
                if (strcmp((char*)&response, "FAL") == 0) {
                    printf("[%d] Ouch! I lost.\n", getpid());
                    break;
                }
                else {
                    if (strcmp((char*)&response, "BTZ") == 0) {
                        printf("[%d] Got from client : BOLTZ\n", getpid());
                    }
                    else {
                        response = ntohl(response);
                        printf("[%d] Got from client : %d\n", getpid(), response);
                        if (isBoltz(response)) {
                            printf("[%d] Client is wrong! I won.\n", getpid());

                            char* failMsg = "FAL";
                            if (sendto(newS, failMsg, 4, 0, (struct sockaddr *) &client, cl) < 0) {
                                error("[C] Error while trying to send FAIL message.\n");
                            }
                            break;
                        }
                    }
                }
                number++;

                // Timeout
                sleep(1);

                // Sending the number, depending on its value and randomness
                if (isBoltz(number) && rand() % 4 > 0) {
                    printf("Sending : BOLTZ\n");
                    char* boltzMsg = "BTZ";
                    if (sendto(newS, boltzMsg, 4, 0, (struct sockaddr *) &client, cl) < 0) {
                        error("[C] Error while trying to send BOLTZ.\n");
                    }
                }
                else {
                    printf("[%d] Sending : %d\n", getpid(), number);
                    number = htonl(number);
                    if (sendto(newS, &number, sizeof(uint32_t), 0, (struct sockaddr *) &client, cl) < 0) {
                        error("[C] Error while trying to send NUMBER.\n");
                    }
                    number = ntohl(number);
                }
                number++;
            }

            close(newS);
            return 0;
        }
        else {
            printf("[P] Created new child server with PID : %d\n", pid);
        }
    }

    close(s);
}

