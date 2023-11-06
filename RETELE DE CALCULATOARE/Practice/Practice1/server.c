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

    struct sockaddr_in server, client;
    unsigned sl = sizeof(server);
    unsigned cl = sizeof(client);
    memset(&server, 0, sl);
    memset(&client, 0, cl);
    server.sin_family = AF_INET;
    server.sin_port = htons(port);
    server.sin_addr.s_addr = INADDR_ANY;

    if (bind(s, (struct sockaddr *) &server, sl) < 0) {
        error("Error binding.\n");
    }

    if (listen(s, 5) < 0) {
        error("Error while calling listen().\n");
    }

    signal(SIGCHLD, SIG_IGN); // Zombie killer
    signal(SIGPIPE, SIG_IGN);

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

            close(c);

            // ------------------------------------------------------------------

            if (clientsServed % 2 == 1) { // Open as server
                int chld_s = socket(AF_INET, SOCK_DGRAM, 0); // use SOCK_DGRAM for UDP - *skull emoji*
                if (chld_s < 0) {
                    error("Error while creating socket.\n");
                }

                struct sockaddr_in chld_server, chld_client;
                unsigned chld_sl = sizeof(chld_server);
                unsigned chld_cl = sizeof(chld_client);
                memset(&chld_server, 0, chld_sl);
                memset(&chld_client, 0, chld_cl);
                chld_server.sin_family = AF_INET;
                chld_server.sin_port = htons(port + clientsServed);
                chld_server.sin_addr.s_addr = INADDR_ANY;

                if (bind(chld_s, (struct sockaddr *) &chld_server, chld_sl) < 0) {
                    close(chld_s);
                    printf("[%d] ", getpid());
                    error("Error binding.\n");
                }

                printf("[%d] Opened as server\n", getpid());

                uint16_t chld_len;
                char chld_input[MAX_SIZE];
                if (recvfrom(chld_s, &chld_len, sizeof(uint16_t), MSG_WAITALL, (struct sockaddr *) &chld_client, &chld_cl) < 0 ||
                    recvfrom(chld_s, chld_input, ntohs(chld_len), MSG_WAITALL, (struct sockaddr *) &chld_client, &chld_cl) < 0) {
                    close(chld_s);
                    printf("[%d] ", getpid());
                    fflush(stdout);
                    error("Error while receiving string with its length.\n");
                }
                chld_len = ntohs(chld_len);
                chld_input[chld_len] = '\0';

                printf("[%d] Received following string from child client : %s\n", getpid(), chld_input);

                len = htons(len);
                if (sendto(chld_s, &len, sizeof(uint16_t), 0, (struct sockaddr *) &chld_client, chld_cl) < 0 ||
                    sendto(chld_s, input, ntohs(len), 0, (struct sockaddr *) &chld_client, chld_cl) < 0) {
                    close(chld_s);
                    printf("[%d] ", getpid());
                    fflush(stdout);
                    error("Error while sending string with its length.\n");
                }

                close(chld_s);
            }
            else { // Open as client
                int chld_c = socket(AF_INET, SOCK_DGRAM, 0); // use SOCK_DGRAM for UDP - *skull emoji*
                if (chld_c < 0) {
                    error("Error while creating socket.\n");
                }

                struct sockaddr_in chld_server;
                unsigned chld_sl = sizeof(chld_server);
                memset(&chld_server, 0, chld_sl);
                chld_server.sin_family = AF_INET;
                chld_server.sin_port = htons(port + clientsServed - 1);
                chld_server.sin_addr.s_addr = client.sin_addr.s_addr;

                printf("[%d] Opened as client\n", getpid());

                len = htons(len);
                if (sendto(chld_c, &len, sizeof(uint16_t), 0, (struct sockaddr *) &chld_server, chld_sl) < 0 ||
                    sendto(chld_c, input, ntohs(len), 0, (struct sockaddr *) &chld_server, chld_sl) < 0) {
                    close(chld_c);
                    printf("[%d] ", getpid());
                    fflush(stdout);
                    error("Error while sending string with its length.\n");
                }

                uint16_t chld_len;
                char chld_input[MAX_SIZE];
                if (recvfrom(chld_c, &chld_len, sizeof(uint16_t), MSG_WAITALL, (struct sockaddr *) &chld_server, &chld_sl) < 0 ||
                    recvfrom(chld_c, chld_input, ntohs(chld_len), MSG_WAITALL, (struct sockaddr *) &chld_server, &chld_sl) < 0) {
                    close(chld_c);
                    printf("[%d] ", getpid());
                    fflush(stdout);
                    error("Error while receiving string with its length.\n");
                }
                chld_len = ntohs(chld_len);
                chld_input[chld_len] = '\0';

                printf("[%d] Received following string from child server : %s\n", getpid(), chld_input);

                close(chld_c);
            }

            return 0;
        }
        else {
            printf("[P] Created child process with pid : %d\n", pid);
        }
    }

    close(s);

}