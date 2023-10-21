// COMMON.H : The header file for common functions for both client and the server

#ifndef __COMMON_H__
#define __COMMON_H__

#include <stdint.h>

#define INITIAL_BUFFER_SIZE 2

typedef short bool;
#define true 1
#define false 0

bool isIPAddress(const char *str);

bool isValidPort(const char *str);

void displayError(char* message);

char* resizeString(char* string, int* length);

char* readVariableInput();

void sendStringToSocket(int socket_desc, char* text);

char* receiveStringFromSocket(int socket_desc, int flags);

#endif

