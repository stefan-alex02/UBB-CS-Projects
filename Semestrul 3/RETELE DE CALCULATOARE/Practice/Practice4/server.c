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
        error("Error binding.");
    }

    if (listen(s, 5) < 0) {
        error("Error listening.");
    }

    struct sockaddr_in client;
    unsigned cl = sizeof (client);
    memset(&client, 0, cl);

    int servedClients = 0;

    signal(SIGCHLD, SIG_IGN);

    in_addr_t latestIP = 0;
    while(1) {
        int c = accept(s, (struct sockaddr *) &client, &cl);

        servedClients++;

        if(fork() == 0) {
            uint16_t port1;
            if (recv(c, &port1, sizeof(uint16_t), MSG_WAITALL) < 0) {
                error("Error receiving port.");
            }
            port1 = ntohs(port1);

            close(c);

            int sUDP = socket(AF_INET, SOCK_DGRAM, 0);
            if (sUDP < 0) {
                error("Error creating socket.");
            }

            struct sockaddr_in clientUDP;
            unsigned ludp = sizeof (clientUDP);
            memset(&clientUDP, 0, ludp);
            clientUDP.sin_family = AF_INET;
            clientUDP.sin_port = htons(port1);
            clientUDP.sin_addr.s_addr = client.sin_addr.s_addr;

            printf("[C] Send?\n");
            getchar();

            if (sendto(sUDP, &latestIP, sizeof(in_addr_t), 0,
                       (struct sockaddr *) &clientUDP, ludp) < 0) {
                error("Error sending IP ADDRESS.");
            }

            printf("[C] IP sent.\n");

            close(sUDP);

            return 0;
        }
        else {
            printf("[P] Created child server.\n");
            latestIP = client.sin_addr.s_addr;
        }
    }

    close(s);

    return 0;
}

