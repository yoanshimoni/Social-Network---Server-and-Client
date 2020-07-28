package bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.MessegesTypes.MessageType;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.AckMes;
import bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessage;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.ErrorMes;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.NotificationMes;
import bgu.spl.net.srv.SystemData;

public class PMReq extends ClientToServerMessage {

    private String userNameToSend;
    private String content;

    public PMReq(String decodedMessage) {
        super(decodedMessage);
        init();
    }

    protected void init(){
        String[] splited = decodedMessage.split("\0");
        userNameToSend = splited[0];
        content = splited[1];
    }

    public void execute(int connectionId, SystemData data, Connections connections, BidiMessagingProtocolImpl bidiMessagingProtocol){
        String userName = data.searchLoggedInUser(connectionId);
        if(userName != null && data.isRegistered(userNameToSend)){
            data.addPostOrPm(userName, this);
            if (data.isLoggedInByUserName(userNameToSend)) {
                connections.send(data.getId(userNameToSend), new NotificationMes((short) 6, MessageType.PM_REQ, userName, content));
            }
            else {
                data.addNotification(userNameToSend, new NotificationMes((short) 5, MessageType.PM_REQ, userName, content));
            }
            connections.send(connectionId, new AckMes((short) 6));
        }
        else {
            connections.send(connectionId, new ErrorMes((short) 6));
        }
    }
}


