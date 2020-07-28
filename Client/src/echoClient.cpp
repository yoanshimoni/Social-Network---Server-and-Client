#include <stdlib.h>
#include <connectionHandler.h>
#include "GetFromServerTask.h"
#include "KeyboardAndSendTask.h"
#include <thread>

using namespace std;


/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/



int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    bool ack(false);
    bool error(false);
    KeyboardAndSendTask keyboardandsendtask(&connectionHandler, ack, error);
    GetFromServerTask getterTask(&connectionHandler, ack, error);
    std :: thread keyboard(&KeyboardAndSendTask::run, &keyboardandsendtask);
    std :: thread getter(&GetFromServerTask::run, &getterTask);

    keyboard.join();
    getter.join();

    return 0;

}
