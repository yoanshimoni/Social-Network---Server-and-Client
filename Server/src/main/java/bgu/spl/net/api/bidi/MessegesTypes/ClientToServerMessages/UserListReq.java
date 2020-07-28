package bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.AckMesUserList;
import bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessage;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.ErrorMes;
import bgu.spl.net.srv.SystemData;

import java.util.List;


public class UserListReq extends ClientToServerMessage {

    public UserListReq(String decodedMessage){
        super(decodedMessage);
    }

    protected void init(){} // a function that my be implemented its inherits

    public void execute(int connectionId, SystemData data, Connections connections, BidiMessagingProtocolImpl bidiMessagingProtocol){
        String userName = data.searchLoggedInUser(connectionId);
        if(userName == null) {
            connections.send(connectionId, new ErrorMes((short) 7));
        }
        else{
            List<String> registeredUsers = data.getAllRegisteredUsers();
            connections.send(connectionId, new AckMesUserList((short) 7, (short) registeredUsers.size(), registeredUsers));
        }
    }
}
