//
// Created by Guy Bercovich on 01/01/2019.
//

#include "GetFromServerTask.h"


//TODO: WHAT ABOUY TERMINATION?
GetFromServerTask:: GetFromServerTask(ConnectionHandler* handler1, bool& _ack, bool& _error) :  handler(handler1), isTerminate(false), ack(_ack), error(_error){}
void GetFromServerTask::run() {
    while (!isTerminate) {
        string toPrint;
        char opCodeChar[2];
        (*handler).getBytes(opCodeChar, 2);
        short opcodeShort = bytesToShort(opCodeChar);

        //NOTIFICATION
        if (opcodeShort == 9) {
            string postingUser;
            string content;
            toPrint = "NOTIFICATION ";
            char pmOrPublicChar[1];
            (*handler).getBytes(pmOrPublicChar, 1);
            short pmOrPublicShort = byteToShort(pmOrPublicChar[0]);
            if(pmOrPublicShort == 0){
                toPrint = toPrint + "PM ";
            }
            else{
                toPrint = toPrint + "Public ";
            }
            (*handler).getLine(postingUser);
            (*handler).getLine(content);
            toPrint = toPrint + postingUser.substr(0, postingUser.length() - 1) + " " + content.substr(0, content.length() -1);
        }
         //ACK
        else if (opcodeShort == 10) {
            char msgOpcodeChar[2];
            (*handler).getBytes(msgOpcodeChar, 2);
            short msgOpcodeShort = bytesToShort(msgOpcodeChar);
            toPrint = "ACK " + to_string(msgOpcodeShort);
            if (msgOpcodeShort == 4 || msgOpcodeShort == 7) {// FOLLOW or USERLIST
                char numOfUsersChar[2];
                (*handler).getBytes(numOfUsersChar, 2);
                short numOfUsersShort = bytesToShort(numOfUsersChar);
                toPrint = toPrint + " " + to_string(numOfUsersShort) +
                          " ";  //TODO: add space bewtte toPrint and optional?
                string userName;
                //       (*handler).getLine(userName);
                int numOfInsertedUsers = 0;
                while (numOfInsertedUsers < numOfUsersShort) {
                    (*handler).getLine(userName);
                    userName = userName.substr(0, userName.find('\0'));
                    numOfInsertedUsers++;
                    if (numOfInsertedUsers < numOfUsersShort)
                        toPrint = toPrint + userName + " ";
                    else
                        toPrint = toPrint + userName;
                    userName = "";
                }
                // STAT
            } else if (msgOpcodeShort == 8) {    // STAT
                char numPostChar[2];
                (*handler).getBytes(numPostChar, 2);
                short numPostShort = bytesToShort(numPostChar);
                toPrint = toPrint + " " + to_string(numPostShort);
                char numFollowersChar[2];
                (*handler).getBytes(numFollowersChar, 2);
                short numFollowersShort = bytesToShort(numFollowersChar);
                toPrint = toPrint + " " + to_string(numFollowersShort);
                char numFollowingChar[2];
                (*handler).getBytes(numFollowingChar, 2);
                short numFollowingShort = bytesToShort(numFollowingChar);
                toPrint = toPrint + " " + to_string(numFollowingShort);
            }
            else if(msgOpcodeShort == 3){
                    ack = true;
                    isTerminate = true;
            }

        }
            //ERROR
        else if(opcodeShort == 11){ // opcode == 11
            char msgOpcodeChar[2];
            (*handler).getBytes(msgOpcodeChar, 2);
            short msgOpcodeShort = bytesToShort(msgOpcodeChar);
            toPrint = "ERROR " + to_string(msgOpcodeShort);
            //TODO: take care of the logout specifically (msgOpcode == 3)
            if(msgOpcodeShort == 3){
                error = true; //TODO: CHECK IF THE * WORKING
            }
        }
        else
            toPrint = "invalid opcode from server";
        cout << toPrint << endl;
    }
}


short GetFromServerTask:: bytesToShort(char* bytesArr) {
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

short GetFromServerTask::byteToShort(char byte)
{
    return (short)((byte & 0xf));
}



