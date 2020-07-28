package bgu.spl.net.api.bidi;


import bgu.spl.net.srv.SystemData;

public abstract class Message {

    public Message(){
    }

    public abstract void execute(int connectionId, SystemData data, Connections connections, BidiMessagingProtocolImpl bidiMessagingProtocol);

}
