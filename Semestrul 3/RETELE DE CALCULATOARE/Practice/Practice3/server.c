#include <stdio.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <errno.h>
#include <string.h>
#include <sys/socket.h>
#include <unistd.h>
#include <signal.h>

#define MAX_SIZE 101

int error(char* str) {
    if (errno != 0) {
        perror(str);
    }
    else {
        fprintf(stderr, "%s", str);
    }
    exit(EXIT_FAILURE);
}

uint16_t get_port(char* portStr) {
    if (portStr == NULL) {
        error("Port argument cannot be NULL.\n");
    }
    char* text = NULL;
    long port = strtol(portStr, &text, 10);
    if (strlen(text) != 0 || (port == 0 && portStr[0] != '0')) {
        error("Port string is invalid.\n");
    }
    if (!(port >= 0 && port <= 65535)) {
        error("Port is not a 2 byte number.\n");
    }
    return (uint16_t)port;
}

void printIpAddress(struct in_addr inAddr) {
    uint32_t ipAddress = ntohl(inAddr.s_addr);
    printf("IP ADDRESS : %hhu.%hhu.%hhu.%hhu\n",
           (uint8_t)((uint8_t *)&ipAddress)[3],
           (uint8_t)((uint8_t *)&ipAddress)[2],
           (uint8_t)((uint8_t *)&ipAddress)[1],
           (uint8_t)((uint8_t *)&ipAddress)[0]);
}

int main(int argc, char** argv) {
    if (argc != 2) {
        printf("Invalid number of arguments.\nUsage: %s [PORT]", argv[0]);
        exit(EXIT_FAILURE);
    }
    int port = get_port(argv[1]);

    int clientsServed = 0;

    int s = socket(AF_INET, SOCK_STREAM, 0);
    if (s < 0) {
        error("Error while creating socket.\n");
    }

    struct sockaddr_in server;
    unsigned sl = sizeof(server);
    memset(&server, 0, sl);
    server.sin_family = AF_INET;
    server.sin_port = htons(port);
    server.sin_addr.s_addr = INADDR_ANY;

    struct sockaddr_in client;
    unsigned cl = sizeof(client);
    memset(&client, 0, cl);

    if (bind(s, (struct sockaddr *) &server, sl) < 0) {
        error("Error binding.\n");
    }

    if (listen(s, 5) < 0) {
        error("Error while calling listen().\n");
    }

    signal(SIGCHLD, SIG_IGN); // Zombie killer

    while(1) {
        int pid;
        int c = accept(s, (struct sockaddr *) &client, &cl);
        clientsServed++;

        if ((pid = fork()) == 0) {  // In child process
            uint16_t len;
            char input[MAX_SIZE];
            if (recv(c, &len, sizeof(uint16_t), MSG_WAITALL) < 0 ||
                recv(c, input, ntohs(len), MSG_WAITALL) < 0) {
                printf("[%d] ", getpid());
                error("Error while receiving string with its length");
            }
            len = ntohs(len);
            input[len] = '\0';

            printf("[%d] Received string : %s\n", getpid(), input);

            printIpAddress(client.sin_addr);

            close(c);

            return 0;
        }
        else {
            printf("[P] Created child process with pid : %d\n", pid);
        }
    }

    close(s);

}
