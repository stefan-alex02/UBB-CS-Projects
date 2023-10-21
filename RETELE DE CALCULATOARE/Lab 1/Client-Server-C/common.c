// COMMON.C : Common functions for both client and server file

#include "common.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <arpa/inet.h>

bool isIPAddress(const char *str) {
    struct in_addr addr;
    return inet_pton(AF_INET, str, &addr) == true;
}

bool isValidPort(const char *str) {
    // Checking if the string is numeric first
    for (int i = 0; str[i]; i++) {
        if (!(str[i] >= '0' && str[i] <= '9')) {
            return false;
        }
    }
    return atoi(str) != 0;
}

void displayError(char* message) {
    perror(message);
    exit(EXIT_FAILURE);
}

char* resizeString(char* string, int* length) {
    *length = *length * 2;

    string = (char*) realloc(string, *length);

    if (string == NULL) {
        displayError("Error allocating memory.\n");
    }

    return string;
}

char* readVariableInput() {
    int bufferSize = INITIAL_BUFFER_SIZE;
    char* buffer = (char*) malloc(bufferSize);

    if (buffer == NULL) {
        displayError("Error allocating memory.\n");
    }

    char c;
    int i = 0;

    while ( (c = getchar()) != EOF && c != '\n') {
        buffer[i++] = c;

        if (i >= bufferSize - 1) {
            buffer = resizeString(buffer, &bufferSize);
        }
    }

    return buffer;
}

void sendStringToSocket(int socket_desc, char* text) { // Sends the length first, and then the string itself
    uint16_t len = (text == NULL ? 0 : strlen(text));
    uint16_t rlen = htons(len);
    send(socket_desc, &rlen, sizeof(uint16_t), 0);
    if (len > 0) {
        send(socket_desc, text, len, 0);
    }
}

char* receiveStringFromSocket(int socket_desc, int flags) { // Gets the length first, and then the string itself
    uint16_t bufferSize;
    char* buffer = NULL;

    recv(socket_desc, &bufferSize, sizeof(uint16_t), flags);
    bufferSize = ntohs(bufferSize);

    buffer = (char*) malloc(bufferSize + 1);
    if (buffer == NULL) {
        displayError("Error allocating memory.\n");
    }
    if (bufferSize > 0) {
        recv(socket_desc, buffer, bufferSize, flags);
    }
    buffer[bufferSize] = '\0';

    return buffer;
}
