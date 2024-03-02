#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <arpa/inet.h>
#include <unistd.h>

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

struct in_addr get_ip_address(char* ipAddressStr) {
    if (ipAddressStr == NULL) {
        error("IP address argument cannot be NULL.\n");
    }
    struct in_addr ipAddress;
    if (inet_aton(ipAddressStr, &ipAddress) == 0) {
        error("IP address is invalid.\n");
    }
    return ipAddress;
}

int main(int argc, char** argv) {
    if (argc != 3) {
        fprintf(stderr, "Invalid number of arguments.\nUsage: %s [IP ADDRESS] [PORT]\n", argv[0]);
        exit(EXIT_FAILURE);
    }
    struct in_addr ip_address = get_ip_address(argv[1]);
    uint16_t port = get_port(argv[2]);

    int c = socket(AF_INET, SOCK_STREAM, 0);
    if (c < 0) {
        error("Error: couldn't create socket.\n");
    }

    struct sockaddr_in server;
    unsigned l = sizeof(server);
    memset(&server, 0, l);
    server.sin_family = AF_INET;
    server.sin_port = htons(port);
    server.sin_addr = ip_address;

    if (connect(c, (struct sockaddr *) &server, l) < 0) {
        error("Error connecting to server.\n");
    }

    char input[MAX_SIZE];
    printf("Give input string : ");
    if ( fgets(input, MAX_SIZE, stdin) == NULL) {
        error("Error while reading input.\n");
    }
    input[strlen(input) - 1] = '\0';
    uint16_t len = htons(strlen(input));

    if (send(c, &len, sizeof(uint16_t), 0) < 0 ||
        send(c, input, strlen(input), 0) < 0) {
        error("Error while sending string and its length.\n");
    }

    close(c);

    return 0;
}
