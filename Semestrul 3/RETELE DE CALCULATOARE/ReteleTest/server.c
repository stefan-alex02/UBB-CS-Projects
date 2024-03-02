#include <stdio.h>
#include <errno.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>

void error(char* string) {
    if (errno != 0) {
        perror(string);
    }
    else {
        fprintf(stderr, "%s", string);
    }
    exit(EXIT_FAILURE);
}

uint16_t get_port(char* str) {
    char* text = NULL;
    long port;
    if (((port = strtol(str, &text, 10)) == 0 && str[0] != '0') ||
        strlen(text) > 0 ||
        !(port >= 0 && port <= 65535)) {
        error("Invalid port.");
    }
    return (uint16_t)port;
}

void printIP(in_addr_t ip) {
    ip = ntohl(ip);
    printf("[%d] Client IP ADDRESS : %hhu.%hhu.%hhu.%hhu\n", getpid(),
           (uint8_t)(((uint8_t*)&ip)[3]),
           (uint8_t)(((uint8_t*)&ip)[2]),
           (uint8_t)(((uint8_t*)&ip)[1]),
           (uint8_t)(((uint8_t*)&ip)[0]))  ;
}

int main(int argc, char** argv) {
    if (argc != 2) {
        fprintf(stderr, "Invalid no of arguments.\nUsage: %s [PORT]\n", argv[0]);
        exit(EXIT_FAILURE);
    }
    uint16_t port = get_port(argv[1]);

    int s = socket(AF_INET, SOCK_STREAM, 0);
    if (s < 0) {
        error("Error creating socket.");
    }

    struct sockaddr_in server;
    unsigned l = sizeof (server);
    memset(&server, 0, l);
    server.sin_family = AF_INET;
    server.sin_port = htons(port);
    server.sin_addr.s_addr = INADDR_ANY;

    if (bind(s, (struct sockaddr *) &server, l) < 0) {
        error("Error binding");
    }

    if (listen(s, 5) < 0) {
        error("Error listening");
    }

    struct sockaddr_in client;
    unsigned cl = sizeof (client);
    memset(&client, 0, cl);

    signal(SIGCHLD, SIG_IGN); // Zombie killer

    while(1) {
        int pid;
        int c = accept(s, (struct sockaddr *) &client, &cl);

        if((pid = fork()) == 0) {
            uint32_t deimpartit;
            if (recv(c, &deimpartit, sizeof(uint32_t), MSG_WAITALL) < 0) {
                error("Error receiving deimpartit.");
            }
            deimpartit = ntohl(deimpartit);

            uint32_t impartitor;
            if (recv(c, &impartitor, sizeof(uint32_t), MSG_WAITALL) < 0) {
                error("Error receiving impartitor.");
            }
            impartitor = ntohl(impartitor);

            uint16_t p;
            if (recv(c, &p, sizeof(u_int16_t), MSG_WAITALL) < 0) {
                error("Error receiving port.");
            }
            p = ntohs(p);

            printf("[%d] Received deimpartit : %d\n", getpid(), deimpartit);
            printf("[%d] Received impartitor : %d\n", getpid(), impartitor);
            printf("[%d] Received port : %hu\n", getpid(), p);

            printIP(client.sin_addr.s_addr);
            printf("[%d] Client port : %hu\n", getpid(), ntohs(client.sin_port));

            close(c);

            // Opening UDP
            int sUDP = socket(AF_INET, SOCK_DGRAM, 0);
            if (sUDP < 0) {
                error("[S] Error creating socket.");
            }

            struct sockaddr_in clientUDP;
            unsigned ludp = sizeof (clientUDP);
            memset(&clientUDP, 0, ludp);
            clientUDP.sin_family = AF_INET;
            clientUDP.sin_port = htons(p);
            clientUDP.sin_addr.s_addr = client.sin_addr.s_addr;

            printf("[%d] Process and send?\n", getpid());
            getchar();

            // Processing request
            uint16_t code;
            uint32_t cat, rest;
            if (impartitor == 0) {
                code = 401;

                code = htons(code);
                if (sendto(sUDP, &code, sizeof(uint16_t), 0,
                           (struct sockaddr *) &clientUDP, ludp) < 0) {
                    error("[S] Error sending code.");
                }
            }
            else {
                code = 200;
                cat = deimpartit / impartitor;
                rest = deimpartit % impartitor;

                code = htons(code);
                if (sendto(sUDP, &code, sizeof(uint16_t), 0,
                           (struct sockaddr *) &clientUDP, ludp) < 0) {
                    error("[S] Error sending code.");
                }

                cat = htonl(cat);
                if (sendto(sUDP, &cat, sizeof(uint32_t), 0,
                           (struct sockaddr *) &clientUDP, ludp) < 0) {
                    error("[S] Error sending cat.");
                }

                rest = htonl(rest);
                if (sendto(sUDP, &rest, sizeof(uint32_t), 0,
                           (struct sockaddr *) &clientUDP, ludp) < 0) {
                    error("[S] Error sending rest.");
                }
            }

            printf("[C] Data sent to specified port and ip address.\n");

            close(sUDP);

            return 0;
        }
        else {
            printf("[P] Created child server with PID : %d.\n", pid);
        }
    }

    close(s);

    return 0;
}