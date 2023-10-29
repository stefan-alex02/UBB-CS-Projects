cmake_minimum_required(VERSION 3.27)
project(Lab_1c_Client_Server_C C)

set(CMAKE_C_STANDARD 11)

add_executable(Client clientUDP.c)

add_executable(Server serverUDP.c)