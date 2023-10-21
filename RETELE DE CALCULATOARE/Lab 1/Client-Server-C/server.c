// Laborator 1 - Problema 4 : Un client trimite unui server doua siruri de caractere ordonate.
// Serverul va interclasa cele doua siruri si va returna clientului sirul rezultat interclasat.

// SERVER.C : The server receives the 2 ordered strings from the client, merges them (if possible)
// and sends a status code with the resulted string (if successful).
// The response format: [the status code given in 16 bits (2 bytes)]
// [length of the string, written in 2 bytes(uint16_t)]
// [the string of chars(each character is a byte)]

#include <sys/types.h>
#include <sys/socket.h>
#include <sys/wait.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdio.h>

#include <stdlib.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <signal.h>

#include "common.h"

//#define PORT 12345

void zombie_killer() { // Kills the process that is waited
    wait(0);
}

void mergeStrings(const char* s1, const char* s2, char* s3) {
    int i = 0, j = 0, k = 0;

    while (s1[i] && s2[j]) {
        if (s1[i] < s2[j]) {
            s3[k++] = s1[i++];
        }
        else {
            s3[k++] = s2[j++];
        }
    }

    while (s1[i]) {
        s3[k++] = s1[i++];
    }

    while (s2[j]) {
        s3[k++] = s2[j++];
    }

}

void serve_client(int c) {
    int current_pid = getpid();

    char *s1 = NULL, *s2 = NULL;    // The request from the client
    uint16_t code;                  // The server response status code
    char* s3 = NULL;                // The server response string

    printf("[%d] > Waiting to receive request from client...\n", current_pid);

    // Receive the request from the client
    s1 = receiveStringFromSocket(c, MSG_WAITALL);
    s2 = receiveStringFromSocket(c, MSG_WAITALL);
    printf("[%d] > Received the following strings :\n[Str 1] \"%s\"\n[Str 2] \"%s\"\n", current_pid, s1, s2);

    // No need to convert the request to host byte order because it only consists of chars(bytes)

    printf("[%d] > Processing the request...\n", current_pid);

    // Processing the request :
    // - If any of the given strings was empty, the status code will be 401
    if (strlen(s1) == 0 || strlen(s2) == 0) {
        code = 401;
    }
        // - Else, status code will be 200
    else {
        code = 200;

        // Merging the strings
        s3 = (char*) malloc(strlen(s1) + strlen(s2) + 1);
        if (s3 == NULL) {
            displayError("Error allocating memory.\n");
        }

        mergeStrings(s1, s2, s3);
    }
    // Freeing the memory for the received strings
    free(s1);
    free(s2);

    // Converting the status code to network byte order
    code = htons(code);

    // Send the response to the client
    printf("[%d] > Sending the response to the client...\n", current_pid);
    send(c, &code, sizeof(uint16_t), 0);
    sendStringToSocket(c, s3);

    // Freeing the memory
    free(s3);

    // Close current client socket
    printf("[%d] > Repose sent. Ending the connection...\n", current_pid);
    close(c);
}

int main(int argc, char** argv) {
    if (argc != 2) {
        fprintf(stderr, "Invalid number of arguments.\nUsage %s [PORT].\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    if (isValidPort(argv[1]) == false) {
        displayError("Error: PORT is invalid.\n");
    }
    int PORT = atoi(argv[1]);

    int s;                              // The server socket descriptor
    struct sockaddr_in server, client;  // Structures for server and client addresses
    int c;                           // The client socket descriptor and client address length
    uint32_t l;

    // Create the socket
    if ( ( s = socket(AF_INET, SOCK_STREAM, 0) ) < 0) {
        displayError("Error creating server socket.\n");
    }
    else {
        printf("[P] > Socket created successfully.\n");
    }

    // Initialize server address structure
    memset(&server, 0, sizeof(server));
    server.sin_port = htons(PORT);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    // Bind the socket to the server address
    if ( bind(s, (struct sockaddr *) &server, sizeof(server)) < 0) {
        displayError("Error binding\n");
    }
    else {
        printf("[P] > Binding finished successfully.\n");
    }

    // Listen for incoming connections
    printf("[P] > Listening for incoming connections...\n");
    listen(s, 5);

    // Initialize the client socket
    l = sizeof(client);
    memset(&client, 0, sizeof(client));

    signal(SIGCHLD, zombie_killer); // Setting the zombie process killer

    while(1) {
        int pid;    // Process ID

        // Accept a connection from a client
        c = accept(s, (struct sockaddr *) &client, &l);
        printf("[P] > A client has connected.\n");

        if ((pid = fork()) == 0) {
            serve_client(c);
            return 0;
        }
        else {
            printf("[P] > Created new process with PID : [%d].\n", pid);
        }
    }

    // Close server socket
    close(s);

    return 0;
}
