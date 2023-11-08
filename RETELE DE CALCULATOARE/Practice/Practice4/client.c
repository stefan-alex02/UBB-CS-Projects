#include <stdio.h>
#include <errno.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <unistd.h>

void error(char* string) {
    if (errno != 0) {
        perror(string);
    }
    else {
        fprintf(stderr, "%s", string);
    }
    exit(EXIT_FAILURE);
}

in_addr_t get_ip(char* str) {
    struct in_addr address;
    if (inet_aton(str, &address) == 0) {
        error("Invalid ip address.");
    }
    return address.s_addr;
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
    printf("Received IP : %hhu.%hhu.%hhu.%hhu\n",
           (uint8_t)(((uint8_t*)&ip)[3]),
           (uint8_t)(((uint8_t*)&ip)[2]),
           (uint8_t)(((uint8_t*)&ip)[1]),
           (uint8_t)(((uint8_t*)&ip)[0]))  ;
}

int main(int argc, char** argv) {
    if (argc != 3) {
        fprintf(stderr, "Invalid no of arguments.\nUsage: %s [IP ADDRESS] [PORT]\n", argv[0]);
        exit(EXIT_FAILURE);
    }
    in_addr_t address = get_ip(argv[1]);
    uint16_t port = get_port(argv[2]);

    int c = socket(AF_INET, SOCK_STREAM, 0);
    if (c < 0) {
        error("Error creating socket.");
    }

    struct sockaddr_in server;
    unsigned l = sizeof (server);
    memset(&server, 0, l);
    server.sin_family = AF_INET;
    server.sin_port = htons(port);
    server.sin_addr.s_addr = address;

    if (connect(c, (struct sockaddr *) &server, l) < 0) {
        error("Error connecting.");
    }

    uint16_t port1;
    printf("Give port: ");
    if (scanf("%hu", &port1) < 1) {
        error("Error reading.");
    }
    port1 = htons(port1);

    if (send(c, &port1, sizeof(uint16_t), 0) < 0) {
        error("Error sending port.");
    }

    close(c);

    uint16_t waitPort;
    printf("Give waiting port: ");
    if (scanf("%hu", &waitPort) < 1) {
        error("Error reading.");
    }

    int cUDP = socket(AF_INET, SOCK_DGRAM, 0);
    if (cUDP < 0) {
        error("Error creating socket.");
    }

    struct sockaddr_in clientUDP;
    unsigned ludp = sizeof (clientUDP);
    memset(&clientUDP, 0, ludp);
    clientUDP.sin_family = AF_INET;
    clientUDP.sin_port = htons(waitPort);
    clientUDP.sin_addr.s_addr = INADDR_ANY;

    if (bind(cUDP, (struct sockaddr *) &clientUDP, ludp) < 0) {
        error("Error binding client.");
    }

    in_addr_t ip;
    struct sockaddr_in serverUDP;
    unsigned sludp = sizeof (serverUDP);
    memset(&serverUDP, 0, sludp);

    if (recvfrom(cUDP, &ip, sizeof(in_addr_t), MSG_WAITALL,
            (struct sockaddr *) &serverUDP, &sludp) < 0) {
        error("Error receiving ip.");
    }
    ip = ntohl(ip);

    printIP(ip);

    close(cUDP);

    return 0;
}
