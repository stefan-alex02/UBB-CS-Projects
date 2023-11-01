import ctypes
import socket
import sys

# Checking command line args
IP_ADDRESS = sys.argv[1]
PORT = int(sys.argv[2])
print(f"> Client will be connecting to IP {IP_ADDRESS} with PORT {PORT}")

# Create a socket object
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Connect to the server
server_address = (IP_ADDRESS, PORT)
client_socket.connect(server_address)
print(f"Connected to {server_address[0]}:{server_address[1]}")

# Send data to the server
input1 = ''.join(input("Type 1st string : "))
input2 = ''.join(sorted(input("Type 2nd string : ")))
client_socket.send(ctypes.c_ushort(len(input1)))
client_socket.send(input1.encode()) # foreach
client_socket.send(ctypes.c_ushort(len(input2)))
client_socket.send(input2.encode())
print("> Data sent with success.")

# Receive a response from the server
status_code = client_socket.recv(2).decode()
print(f"Server response:\nStatus code: {status_code}")
if status_code == 200:
    length = int(client_socket.recv(2))
    string = client_socket.recv(length).decode()
    print("Result string is : ", string)
elif status_code == 400:
    print("Error: input strings must not be empty.")

# Close the connection
client_socket.close()