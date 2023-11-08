cmake_minimum_required(VERSION 3.27)
project(Practice3 C)

set(CMAKE_C_STANDARD 11)

add_executable(Client1 client.c)
add_executable(Client2 client.c)
add_executable(Server server.c)
