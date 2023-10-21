cmake_minimum_required(VERSION 3.26)
project(Lab_1_C C)

set(CMAKE_C_STANDARD 11)

add_executable(Client client.c
        common.c)
add_executable(Server server.c
        common.c)
