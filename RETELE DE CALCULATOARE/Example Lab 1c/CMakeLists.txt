cmake_minimum_required(VERSION 3.27)
project(Example_Lab_1c C)

set(CMAKE_C_STANDARD 11)

add_executable(Client clientUDP.c)
add_executable(Server serverUDP.c)
