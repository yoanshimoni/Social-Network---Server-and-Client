package bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages;


import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessage;
import bgu.spl.net.srv.SystemData;

public class ErrorMes extends ServerToClientMessage {

    public ErrorMes(short messageOpcode){
        super(messageOpcode);
    }


    public byte[] toBytes(){ // a function that my be implemented its inherits
        return null;
    }

    public void execute(int connectionId, SystemData data, Connections connections, BidiMessagingProtocolImpl bidiMessagingProtocol){} // a function that my be implemented its inherits
}
