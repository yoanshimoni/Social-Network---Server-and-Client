package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.SystemData;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {

    private int connectionId;
    private Connections connections;
    private boolean shouldTerminate = false;
    private SystemData data;

    public BidiMessagingProtocolImpl(SystemData data){//TODO:: DELETE THE CONSTRACTOR
        this.data = data;
    }

    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.connections = connections;
        this.connectionId = connectionId;
    }

    @Override
    public void process(Message message) {
        message.execute(connectionId, data, connections, this);

    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }

    public void terminateProgress(){
        shouldTerminate = true;
    }
}
