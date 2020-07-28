//
// Created by Guy Bercovich on 01/01/2019.
//


#include "KeyboardAndSendTask.h"
#include "connectionHandler.h"

using namespace std;

KeyboardAndSendTask::KeyboardAndSendTask(ConnectionHandler* handler1, bool& _ack, bool& _error) :  handler(handler1), isTerminate(false), ack(_ack), error(_error){}

void KeyboardAndSendTask:: run() {
    while(!isTerminate) {
        string keyboardInput;
        string line;
        getline(cin, keyboardInput);

        string commandType = keyboardInput.substr(0, keyboardInput.find(' '));
        char opCode[2];
        string command = keyboardInput.substr(keyboardInput.find(' ') + 1,
                                              keyboardInput.length() - 1 - commandType.length());
        if (commandType == "REGISTER") {
            shortToBytes((short) 1, opCode);
            string userName = command.substr(0, command.find(' '));
            string password = command.substr(userName.length() + 1, command.length() - 1 - userName.length());
            (*handler).sendBytes(opCode, 2);
            line = userName + '\0' + password;
            (*handler).sendLine(line);

        } else if (commandType == "LOGIN") {
            shortToBytes((short) 2, opCode);
            string userName = command.substr(0, command.find(' '));
            string password = command.substr(userName.length() + 1, command.length() - 1 - userName.length());
            (*handler).sendBytes(opCode, 2);
            line = userName + '\0' + password;
            (*handler).sendLine(line);

        } else if (commandType == "LOGOUT") {
            shortToBytes((short) 3, opCode);
            (*handler).sendBytes(opCode, 2);
            while (!ack & !error){}
            if(ack){
                isTerminate = true;
            }
            error = false;
        }

        else if (commandType == "FOLLOW") {
            shortToBytes((short) 4, opCode);
            (*handler).sendBytes(opCode, 2);
            char followUnfollowChar[1];
            string followUnFollowString = (command.substr(0, command.find(" ")));
            if (followUnFollowString == "0")
                followUnfollowChar[0] = 0;
            else
                followUnfollowChar[0] = 1;
            (*handler).sendBytes(followUnfollowChar, 1);
            command = command.substr(2, command.length() - 1);
            short numOfUsersShort = (short) stoi(command.substr(0, command.find(" ")));
            char numOfUsersChar[2];
            shortToBytes(numOfUsersShort, numOfUsersChar);
            (*handler).sendBytes(numOfUsersChar, 2);
            command = command.substr(2, command.length() - 1);
            string userList = "";
            int numOfInsertedUsers = 0;
            while (numOfInsertedUsers < numOfUsersShort) {
                string userName;
                if (command.find(" ") != string::npos) {
                    userName = command.substr(0, command.find(" "));
                    command = command.substr(userName.length() + 1, command.length() - userName.length() - 1);
                    userList = userList + userName + '\0';
                } else {
                    userName = command;
                    userList = userList + userName;
                }
                numOfInsertedUsers++;
            }

            (*handler).sendLine(userList);

        } else if (commandType == "POST") {
            shortToBytes((short) 5, opCode);
            (*handler).sendBytes(opCode, 2);
            (*handler).sendLine(command);

        } else if (commandType == "PM") {
            shortToBytes((short) 6, opCode);
            (*handler).sendBytes(opCode, 2);
            string userName = command.substr(0, command.find(" "));
            string content = command.substr(userName.length() + 1, command.length() - userName.length() - 1);
            string toSend = userName + '\0' + content;
            (*handler).sendLine(toSend);

        } else if (commandType == "USERLIST") {
            shortToBytes((short) 7, opCode);
            (*handler).sendBytes(opCode, 2);

        } else if (commandType == "STAT") {
            shortToBytes((short) 8, opCode);
            (*handler).sendBytes(opCode, 2);
            string userName = command.substr(0, command.find(" "));
            (*handler).sendLine(userName);
        }
        else {
            cout << "wrongs input" << endl;
        }
    }
}


void KeyboardAndSendTask:: shortToBytes(int num, char *bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

