package bgu.spl.net.api.bidi;

import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.srv.ConnectionHandler;

public class ConnectionsImpl<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler> clientsMap;

    public ConnectionsImpl(){
        clientsMap = new ConcurrentHashMap<>();
    }

    @Override //@SuppressWarnings("unchecked")
    public boolean send(int connectionId, T msg) {
        if (clientsMap.containsKey(connectionId)) {
            clientsMap.get(connectionId).send(msg);
            return true;
        }
        return false;
    }

    @Override
    public void broadcast(T msg) {
        for(Integer key : clientsMap.keySet())
            clientsMap.get(key).send(msg);
    }

    @Override
    public void disconnect(int connectionId) {
        clientsMap.remove(connectionId);
    }

    public void setClientOnMap(int connectionId, ConnectionHandler connectionHandler){
        clientsMap.put(connectionId, connectionHandler);
    }
}
