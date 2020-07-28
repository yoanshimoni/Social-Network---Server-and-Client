package bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessages;


import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.MessegesTypes.MessageType;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.AckMes;
import bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessage;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.ErrorMes;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.NotificationMes;
import bgu.spl.net.srv.SystemData;

import java.util.LinkedList;
import java.util.List;

public class PostReq extends ClientToServerMessage {

    private String content;
    private List<String> usersToPost = new LinkedList<>();

    public PostReq(String decodedMessage) {
        super(decodedMessage);
        init();
    }

    protected void init() {
        content = decodedMessage.substring(0, decodedMessage.length() - 1);
    }

    private void searchUsersInContent(SystemData data){
        String[] splited = content.split("@");
        for(int i = 1; i < splited.length; i++){
            String name = splited[i];
            if(name.indexOf(' ') != -1) {
                name = name.substring(0, name.indexOf(' '));
                if (data.isRegistered(name) && !usersToPost.contains(name))
                    usersToPost.add(name);
            }
            else{
                if (data.isRegistered(name) && !usersToPost.contains(name))
                    usersToPost.add(name);
            }
        }
    }

    public void execute(int connectionId, SystemData data, Connections connections, BidiMessagingProtocolImpl bidiMessagingProtocol) {
        String userName = data.searchLoggedInUser(connectionId);
        if (userName == null)
            connections.send(connectionId, new ErrorMes((short) 5));
        else {
            searchUsersInContent(data); // add the users that appear in content.
            for(String userToSend : data.whoFollowsMe(userName))  // add my followers
                if(!usersToPost.contains(userToSend))
                    usersToPost.add(userToSend);
            data.addPostOrPm(userName, this);
            connections.send(connectionId, new AckMes((short) 5));
            for (String user: usersToPost){
                if (data.isLoggedInByUserName(user)) {
                    connections.send(data.getId(user), new NotificationMes((short) 5, MessageType.POST_REQ, userName, content));
                }
                else
                    data.addNotification(user, new NotificationMes((short) 5, MessageType.POST_REQ, userName, content));
            }
        }
    }
}
