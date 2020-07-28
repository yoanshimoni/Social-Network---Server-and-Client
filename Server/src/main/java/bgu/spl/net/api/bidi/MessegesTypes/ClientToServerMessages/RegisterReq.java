package bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.AckMes;
import bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessage;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.ErrorMes;
import bgu.spl.net.srv.SystemData;


public class RegisterReq extends ClientToServerMessage {
    private String userName, password;


    public RegisterReq(String decodedMessage) {
        super(decodedMessage);
        init();
    }

    protected void init(){
        String [] splited = decodedMessage.split("\0");
        userName = splited[0];
        password = splited[1];
    }

    public void execute(int connectionId, SystemData data, Connections connections, BidiMessagingProtocolImpl bidiMessagingProtocol){
        if(data.regUser(userName, password, connectionId)){
            connections.send(connectionId, new AckMes((short) 1));
        }
        else{
            connections.send(connectionId, new ErrorMes((short) 1));
        }
    }


}
