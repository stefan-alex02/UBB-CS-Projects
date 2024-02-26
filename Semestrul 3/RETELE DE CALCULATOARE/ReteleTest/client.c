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

int main(int argc, char** argv) {
    if (argc != 3) {
        fprintf(stderr, "Invalid no of arguments.\nUsage: %s [IP ADDRESS] [PORT]\n", argv[0]);
        exit(EXIT_FAILURE);
    }
    in_addr_t address = get_ip(argv[1]);
    uint16_t port = get_port(argv[2]);

    int c = socket(AF_INET, SOCK_STREAM, 0);
    if (c < 0) {
        error("[C] Error creating TCP socket.");
    }

    struct sockaddr_in server;
    unsigned l = sizeof (server);
    memset(&server, 0, l);
    server.sin_family = AF_INET;
    server.sin_port = htons(port);
    server.sin_addr.s_addr = address;

    if (connect(c, (struct sockaddr *) &server, l) < 0) {
        error("[C] Error connecting.");
    }
    printf("[C] Connected to server.\n");

    uint32_t deimpartit;
    printf("[C] Give deimpartit: ");
    if (scanf("%u", &deimpartit) < 1) {
        error("[C] Error reading.");
    }

    uint32_t impartitor;
    printf("[C] Give impartitor: ");
    if (scanf("%u", &impartitor) < 1) {
        error("[C] Error reading.");
    }

    uint16_t p;
    printf("[C] Give port: ");
    if (scanf("%hu", &p) < 1) {
        error("[C] Error reading.");
    }

    // UDP port reading
    uint16_t waitPort;
    printf("[C] Give waiting port: ");
    if (scanf("%hu", &waitPort) < 1) {
        error("[C] Error reading.");
    }

    // Opening UDP
    int cUDP = socket(AF_INET, SOCK_DGRAM, 0);
    if (cUDP < 0) {
        error("[C] Error creating UDP socket.");
    }

    struct sockaddr_in clientUDP;
    unsigned ludp = sizeof (clientUDP);
    memset(&clientUDP, 0, ludp);
    clientUDP.sin_family = AF_INET;
    clientUDP.sin_port = htons(waitPort);
    clientUDP.sin_addr.s_addr = INADDR_ANY;

    if (bind(cUDP, (struct sockaddr *) &clientUDP, ludp) < 0) {
        error("[C] Error binding client.");
    }
    printf("[C] UDP binding finished successfully.\n");

    struct sockaddr_in serverUDP;
    unsigned sludp = sizeof (serverUDP);
    memset(&serverUDP, 0, sludp);

    // Sending data to server through TCP
    deimpartit = htonl(deimpartit);
    if (send(c, &deimpartit, sizeof(uint32_t), 0) < 0) {
        error("[C] Error sending deimpartit.");
    }
    impartitor = htonl(impartitor);
    if (send(c, &impartitor, sizeof(uint32_t), 0) < 0) {
        error("[C] Error sending impartitor.");
    }
    p = htons(p);
    if (send(c, &p, sizeof(uint16_t), 0) < 0) {
        error("[C] Error sending port.");
    }
    printf("[C] Data sent with success.\n");

    // Receiving data from (ex)server through UDP
    uint16_t code;
    uint32_t cat, rest;
    if (recvfrom(cUDP, &code, sizeof(uint16_t), MSG_WAITALL,
                 (struct sockaddr *) &serverUDP, &sludp) < 0) {
        error("[C] Error receiving code.");
    }
    code = ntohs(code);

    switch(code) {
        case 200:
            if (recvfrom(cUDP, &cat, sizeof(uint32_t), MSG_WAITALL,
                         (struct sockaddr *) &serverUDP, &sludp) < 0) {
                error("[C] Error receiving car.");
            }
            cat = ntohl(cat);

            if (recvfrom(cUDP, &rest, sizeof(uint32_t), MSG_WAITALL,
                         (struct sockaddr *) &serverUDP, &sludp) < 0) {
                error("[C] Error receiving rest.");
            }
            rest = ntohl(rest);

            printf("[C] Received cat : %d\n", cat);
            printf("[C] Received rest : %d\n", rest);

            break;
        case 401:
            fprintf(stderr, "[C] Error : division by zero!\n");
            break;
        default:
            fprintf(stderr, "[C] Unknown error occurred when processing request.\n");
    }

    close(cUDP);
    close (c);

    return 0;
}
