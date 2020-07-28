package bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessage;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.AckMes;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.ErrorMes;
import bgu.spl.net.srv.SystemData;

public class LoginReq extends ClientToServerMessage {

    private String userName, password;

    public LoginReq(String decodedMessage) {
        super(decodedMessage);
        init();
    }

    protected void init(){
        String [] splited = decodedMessage.split("\0");
        userName = splited[0];
        password = splited[1];
    }

    public void execute(int connectionId, SystemData data, Connections connections, BidiMessagingProtocolImpl bidiMessagingProtocol){
        if(data.logIn(userName, password, connectionId)){
            connections.send(connectionId, new AckMes((short) 2));
            Message mgsToSend = data.getNotification(userName);
            while (mgsToSend != null){
                connections.send(connectionId, mgsToSend);
                mgsToSend =  data.getNotification(userName);
            }
        }
        else{
            connections.send(connectionId, new ErrorMes((short) 2));
        }

    }
}
