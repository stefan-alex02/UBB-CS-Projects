#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>

#define MAX_BUFFER_SIZE 101

void error(char *s) {
    perror(s);
    exit(EXIT_FAILURE);
}

struct Package {
    uint16_t code;
    uint16_t length;
    char character;
};

int main() {
    int s;
    struct sockaddr_in server, client;
    unsigned l;

    s = socket(AF_INET, SOCK_DGRAM, 0);
    if (s < 0) {
        error("Error while creating server socket.\n");
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(12345);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    if (bind(s, (struct sockaddr *) &server, sizeof(server)) < 0) {
        error("Binding error.\n");
    }

    l = sizeof(client);
    memset(&client, 0, sizeof(client));

    struct Package package;
    char input[MAX_BUFFER_SIZE];

    while (1) {
        if (recvfrom(s, &package, sizeof(struct Package) - 2, MSG_WAITALL,
                     (struct sockaddr *) &client, &l) < 0) {
            error("Error while receiving response from server.\n");
        }
        else if (package.code != 1){
            sendto(s, "RTY", 4, 0,
                   (struct sockaddr *) &client, sizeof(client));
        }
        else {
            break;
        }
    }

    int length = package.length;
    for (int i = 0; i < length; i++) {
        if (recvfrom(s, &package, sizeof(struct Package) - 2, MSG_WAITALL,
                     (struct sockaddr *) &client, &l) < 0) {
            error("Error while receiving response from server.\n");
        }
        else if (package.code != 2){
            sendto(s, "RTY", 4, 0,
                   (struct sockaddr *) &client, sizeof(client));
            i--;
        }
        else {
            input[i] = (char)package.length;
            sendto(s, "ACK", 4, 0,
                   (struct sockaddr *) &client, sizeof(client));
        }
    }

    char buffer[10];
    input[length] = '\0';
    printf("Received the following string :\n%s\n", input);

    package.code = 2;
    for (int i = length - 1; i >= 0; i--) {
        package.length = (uint16_t)input[i];
        sendto(s, &client, sizeof(struct Package) - 2, 0,
               (struct sockaddr *) &client, sizeof(client));

        if (recvfrom(s, buffer, 10, 0,
                     (struct sockaddr *)&client, &l) < 0) {
            error("Error while receiving response from client.\n");
        }
        else if (strcmp(buffer, "ACK") != 0){
            printf("Couldn't send char '%c', retrying...\n", input[i]);
            i++;
        }
    }

    printf("String sent back with success.\n");

    close(s);
}