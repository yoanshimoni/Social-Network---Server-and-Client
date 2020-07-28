package bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.util.List;

public class AckMesFollow extends AckMes{

    private short numOfUsers;
    private List<String> userNameList;

    public AckMesFollow(short messageOpcode, short numOfUsers, List<String> userNameList){
        super(messageOpcode);
        this.numOfUsers = numOfUsers;
        this.userNameList = userNameList;
    }

    public byte[] toBytes(){
        byte[] userList = userListToString(userNameList).getBytes();
        byte[] toSend = new byte[6 + userList.length];
        toSend[0] = shortToBytes((short) 10)[0];
        toSend[1] = shortToBytes((short) 10)[1];
        toSend[2] = shortToBytes( (short) 4)[0];
        toSend[3] = shortToBytes((short) 4)[1];
        toSend[4] = shortToBytes(numOfUsers)[0];
        toSend[5] = shortToBytes(numOfUsers)[1];

        for(int i = 0; i< userList.length; i++)
            toSend[i + 6] = userList[i];
        return toSend;
    }

}
