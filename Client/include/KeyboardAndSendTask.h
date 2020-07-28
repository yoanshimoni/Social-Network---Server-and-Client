//
// Created by Guy Bercovich on 01/01/2019.
//

#ifndef BOOST_ECHO_CLIENT_KEYBOARDANDSENDTAKS_H
#define BOOST_ECHO_CLIENT_KEYBOARDANDSENDTAKS_H


#include <connectionHandler.h>

using namespace std;

class KeyboardAndSendTask {

private:
    ConnectionHandler* handler;
    bool isTerminate;
    bool& ack;
    bool& error;

public:
    KeyboardAndSendTask(ConnectionHandler* handler1, bool& ack, bool& error);

    void run();

    void shortToBytes(int num, char *bytesArr);
};


#endif //BOOST_ECHO_CLIENT_KEYBOARDANDSENDTAKS_H
