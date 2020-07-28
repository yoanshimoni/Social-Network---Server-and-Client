package bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages;


import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.MessegesTypes.MessageType;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessage;
import bgu.spl.net.srv.SystemData;

public class NotificationMes extends ServerToClientMessage {

    private String notificationType;
    private String postingUser;
    private String content;

    public NotificationMes(short messageOpcode, MessageType notificationType, String postingUser, String content){
        super(messageOpcode);
        if (notificationType == MessageType.PM_REQ) //check if PM or POST
            this.notificationType = "0";
        else
            this.notificationType = "1";

        this.postingUser = postingUser;
        this.content = content;
    }

    public byte[] toBytes(){
        byte[] posUser = postingUser.getBytes();
        byte[] cont = content.getBytes();
        byte[] toSend = new byte[5 + posUser.length + cont.length];
        toSend[0] = shortToBytes((short) 9)[0];
        toSend[1] = shortToBytes((short) 9)[1];
        toSend[2] = notificationType.getBytes()[0];
        for(int i = 0; i < posUser.length; i++)
            toSend[i + 3] = posUser[i];
        toSend[3 + posUser.length] = '\0';
        for (int i = 0; i < cont.length; i++)
            toSend[3 + posUser.length + 1 + i] = cont[i];
        toSend[toSend.length - 1] = '\0';
        return toSend;

    }

    public void execute(int connectionId, SystemData data, Connections connections, BidiMessagingProtocolImpl bidiMessagingProtocol){} // a function that my be implemented its inherits

}
