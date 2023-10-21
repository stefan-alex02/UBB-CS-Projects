// Laborator 1 - Problema 4 : Un client trimite unui server doua siruri de caractere ordonate.
// Serverul va interclasa cele doua siruri si va returna clientului sirul rezultat interclasat.

// CLIENT.C : The client sends 2 ordered strings of characters to the server, along with the length of each one.
// The request format:
// [length of the 1st string, written in 2 bytes(uint16_t)]
// [1st string of chars(each character is a byte)]
// [length of the 2nd string, written in 2 bytes(uint16_t)]
// [2nd string of chars(each character is a byte)]

#include <sys/types.h>
#include <sys/types.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

#include <stdlib.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <fcntl.h>

#include "common.h"

//#define PORT 12345
//#define LOCAL_HOST_IP "127.0.0.1"

void sortString(char* str) {
    unsigned int len = strlen(str);
    bool sorted;
    do {
        len--;
        sorted = true;
        for(int i = 0; i < len; i++) {
            if (str[i] > str[i+1]) {
                char temp = str[i];
                str[i] = str[i+1];
                str[i+1] = temp;

                sorted = false;
            }
        }
    } while(!sorted);
}

char* readString(const char* message) {
    printf("%s", message);
    return readVariableInput();
}

int main(int argc, char** argv) {
    if (argc != 3) {
        fprintf(stderr, "Invalid number of arguments.\nUsage %s [IP_ADDRESS] [PORT].\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    if (isValidPort(argv[2]) == false) {
        displayError("Error: PORT is invalid.\n");
    }
    int PORT = atoi(argv[2]);

    if (isIPAddress(argv[1]) == false) {
        displayError("Error: IP address is invalid.\n");
    }
    char* SERVER_IP_ADDRESS = argv[1];

    printf("Client will be connecting to IP: %s, PORT: %d\n", SERVER_IP_ADDRESS, PORT);

    int c;                          // The client socket descriptor
    struct sockaddr_in server ;     // A structure for the server address

    char* s1 = NULL, *s2 = NULL;    // The client request strings
    uint16_t code;                  // The server response status code
    char* s3 = NULL;                // The server response string

    // Creating the socket
    if (( c = socket(AF_INET, SOCK_STREAM, 0) ) < 0) {
        displayError("Error creating client socket.\n");
    }
    else {
        printf("> Socket created successfully.\n");
    }

    // Initialize the server address structure
    memset(&server, 0, sizeof(server));
    server.sin_port = htons(PORT);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr(SERVER_IP_ADDRESS);

    // Uncommenting these lines will force the socket to no longer block during call of
    // connect() function if there is no server opened:

    //int flags = fcntl(c, F_GETFL, 0);
    //fcntl(c, F_SETFL, flags | O_NONBLOCK);

    // Connect to the server
    if ( connect(c, (struct sockaddr *) &server, sizeof(server)) < 0 ) {
        displayError("Error connecting to the server.\n");
    }
    else {
        printf("> Successfully connected to the server.\n");
    }

    // Reading the 2 strings of chars
    s1 = readString("Give the first string : ");
    s2 = readString("Give the second string : ");

    // Sort the given strings
    printf("Sorting the strings...\n");
    sortString(s1);
    sortString(s2);
    printf("The given, sorted, string are :\n[1] \"%s\"\n[2] \"%s\"\n", s1, s2);

    // No need to convert the strings to network byte order because the characters are only 1 byte long

    // Send the strings to the server
    printf("> Sending the strings to the server...\n");
    sendStringToSocket(c, s1);
    sendStringToSocket(c, s2);

    // Freeing the memory
    free(s1);
    free(s2);

    // Wait to receive the server response
    printf("> Waiting for the response...\n");
    recv(c, &code, sizeof(uint16_t), 0);
    s3 = receiveStringFromSocket(c, 0);

    // Convert the response code to host byte order
    code = ntohs(code);

    // Displaying the response, depending on the status code
    printf("Received status code %hu.\n", code);
    switch(code) {
        case 200:
            printf("The result string is :\n[3] \"%s\"\n", s3);
            break;
        case 401:
            printf("Error : input strings must not be empty.\n");
            break;
        default:
            printf("An unknown error occurred.\n");
            break;
    }

    // Freeing the memory
    free(s3);

    // Close the socket
    close(c);

    return 0;
}

