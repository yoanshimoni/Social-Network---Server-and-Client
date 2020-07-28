package bgu.spl.net.api.bidi.MessegesTypes;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.SystemData;

public abstract class ClientToServerMessage extends Message {

    protected String decodedMessage;

    public ClientToServerMessage(String dedcodedMessage){
        this.decodedMessage = dedcodedMessage;
    }

    protected abstract void init();

    public abstract void execute(int connectionId, SystemData data, Connections connections, BidiMessagingProtocolImpl bidiMessagingProtocol);
}
