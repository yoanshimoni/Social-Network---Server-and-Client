package bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.AckMes;
import bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessage;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.ErrorMes;
import bgu.spl.net.srv.SystemData;

public class LogoutReq extends ClientToServerMessage {

    public LogoutReq(String decodedMessage){
        super(decodedMessage);
    }

    protected void init(){}

    public void execute(int connectionId, SystemData data, Connections connections, BidiMessagingProtocolImpl bidiMessagingProtocol){
        String userName = data.searchLoggedInUser(connectionId);
        if(userName != null) {
            data.logOut(connectionId);
            connections.send(connectionId, new AckMes((short) 3));
            bidiMessagingProtocol.terminateProgress();
        }
        else{
            connections.send(connectionId, new ErrorMes((short) 3));
        }
    }
}
