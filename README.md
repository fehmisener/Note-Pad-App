# Note-Pad-App
This program is a notepad application that works synchronously on mobile and desktop. 
It is designed using Socket Programming. The server application of the program is run on the server.
Before running the program, you need to edit the ports, ip and path according to your own server.
The server application must be run before running the client.
In this way, you can run the client anywhere as long as you access the server. At the same time, your notes are stored on your server.

# About Socket Programming
Socket programming is a way of connecting two nodes on a network to communicate with each other. One socket(node) listens on a particular port at an IP, while other socket reaches out to the other to form a connection. Server forms the listener socket while client reaches out to the server.

# Where is Socket Used?
A Unix Socket is used in a client-server application framework. A server is a process that performs some functions on request from a client. Most of the application-level protocols like FTP, SMTP, and POP3 make use of sockets to establish connection between client and server and then for exchanging data.
