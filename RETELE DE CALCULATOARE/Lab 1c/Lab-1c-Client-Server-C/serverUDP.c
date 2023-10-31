#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/wait.h>

#define MAX_BUFFER_SIZE 101

void zombie_killer() { // Kills the process that is waited
    wait(0);
    printf("[P] Zombie process was k*lled.\n");
}

void error(char *s) {
    perror(s);
    exit(EXIT_FAILURE);
}

struct Element {
    uint16_t index;
    char character;
};

struct Package {
    uint16_t code;
    union {
        uint16_t length;
        uint16_t newPort;
        struct Element element;
    };
};

int get_port(char* port) {
    if (port == NULL) {
        error("Error: port is empty.\n");
    }

    char* str_part = NULL;
    long int_part = strtol(port, &str_part, 10);

    if (strlen(str_part) != 0 || (port[0] != '0' && int_part == 0)) {
        error("Error: port is invalid.\n");
    }
    if (int_part < 0 || int_part > 65535) {
        error("Error: port must be a number between [0, 65535].\n");
    }
    return (int)int_part;
}

int create_socket() {
    int s = socket(AF_INET, SOCK_DGRAM, 0);
    if (s < 0) {
        error("Error while creating server socket.\n");
    }
    return s;
}

void bind_socket(int socket, struct sockaddr * socketData, socklen_t len) {
    if (bind(socket, socketData, len) < 0) {
        error("Binding error.\n");
    }
}

int main(int argc, char** argv) {
    if (argc != 2) {
        printf("Invalid number of arguments.\nUsage: %s [PORT]", argv[0]);
        exit(EXIT_FAILURE);
    }

    int s = create_socket();
    struct sockaddr_in server;
    unsigned sl = sizeof(server);
    memset(&server, 0, sl);
    server.sin_port = htons(get_port(argv[1]));
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    bind_socket(s, (struct sockaddr *) &server, sl);

    printf("Main server starting...\n");

    int connectedClients = 0;

    struct sockaddr_in client;
    unsigned cl = sizeof(client);
    memset(&client, 0, cl);

    signal(SIGCHLD, zombie_killer); // Setting the zombie process killer

    while(1) {
        struct Package lengthPackage;

        if (recvfrom(s, &lengthPackage, sizeof(lengthPackage), MSG_WAITALL,
                     (struct sockaddr *) &client, &cl) < 0) {
            error("Error while receiving first package from client.\n");
        }

        lengthPackage.code = ntohs(lengthPackage.code);
        lengthPackage.length = ntohs(lengthPackage.length);

        if (lengthPackage.code != 1) {
            error("Received the wrong package.\n");
        }

        printf("[P] Received package from new client with length value %d.\n", lengthPackage.length);

        connectedClients++;

        if (fork() == 0) {
            printf("[%d] Establishing private connection with new client.\n", getpid());

            // Establishing a new (private...?) connection with the client
            int newS = create_socket();
            struct sockaddr_in newServer;
            unsigned newSl = sizeof(newServer);
            memset(&newServer, 0, newSl);
            newServer.sin_port = htons(get_port(argv[1]) + connectedClients);
            newServer.sin_family = AF_INET;
            newServer.sin_addr.s_addr = INADDR_ANY;

            bind_socket(newS, (struct sockaddr *) &newServer, newSl);

            // Sending the new port to the client (from the old socket)
            struct Package portPackage;
            portPackage.code = htons(2);
            portPackage.newPort = newServer.sin_port;

            sendto(s, &portPackage, sizeof(portPackage), 0,
                   (struct sockaddr *) &client, cl);

            // Receiving the packages from the client
            char input[MAX_BUFFER_SIZE];
            struct Package charPackage;
            for (int i = 0; i < lengthPackage.length; i++) {
                if (recvfrom(newS, &charPackage, sizeof(charPackage), MSG_WAITALL,
                             (struct sockaddr *) &client, &cl) < 0) {
                    error("[Chld] Error while receiving package from client.\n");
                }
                input[i] = charPackage.element.character;
            }

            input[lengthPackage.length] = '\0';
            printf("[%d] Received the following string :\n%s\n", getpid(), input);

            charPackage.code = htons(3);
            for (int i = lengthPackage.length - 1; i >= 0; i--) {
                charPackage.element.character = input[i];
                sendto(newS, &charPackage, sizeof(charPackage), 0,
                       (struct sockaddr *) &client, cl);
            }

            printf("[%d] Reversed string was sent back with success.\n", getpid());

            close(newS);

            return 0;
        }
    }

    close(s);

    printf("Main server is closing...\n");

    return 0;
}