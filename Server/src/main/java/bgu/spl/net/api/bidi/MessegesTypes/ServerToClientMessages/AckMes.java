package bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages;


import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessage;
import bgu.spl.net.srv.SystemData;

import java.util.List;

public class AckMes extends ServerToClientMessage {

    public AckMes(short messageOpcode) {
        super(messageOpcode);
    }

    protected String userListToString(List<String> userNameList){
        String usersOutput = "";
        for(String user : userNameList)
            usersOutput = usersOutput + user + '\0';
        return usersOutput;
    }

    public byte[] toBytes(){ // a function that my be implemented its inherits
        return null;
    }

    public void execute(int connectionId, SystemData data, Connections connections, BidiMessagingProtocolImpl bidiMessagingProtocol){} // a function that my be implemented its inherits
}
