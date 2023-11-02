#include <stdio.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <errno.h>
#include <string.h>
#include <sys/socket.h>
#include <arpa/inet.h>

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
    int port = get_port(argv[2]);

    struct sockaddr_in server;
    unsigned l = sizeof(server);
    memset(&server, 0, l);


    return 0;
}
