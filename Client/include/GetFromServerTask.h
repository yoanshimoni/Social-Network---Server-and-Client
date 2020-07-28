//
// Created by Guy Bercovich on 01/01/2019.
//

#ifndef BOOST_ECHO_CLIENT_GETFROMSERVERTASK_H
#define BOOST_ECHO_CLIENT_GETFROMSERVERTASK_H


#include <connectionHandler.h>

using namespace std;

class GetFromServerTask {
private:
    ConnectionHandler* handler;
    bool isTerminate;
    bool& ack;
    bool& error;
public:
    GetFromServerTask(ConnectionHandler* handler1, bool& _ack, bool& _error);
    void run();
    short bytesToShort(char* bytesArr);
    short byteToShort(char byte);
};


#endif //BOOST_ECHO_CLIENT_GETFROMSERVERTASK_H
