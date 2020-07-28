package bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.AckMesFollow;
import bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessage;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.ErrorMes;
import bgu.spl.net.srv.SystemData;

import java.util.LinkedList;
import java.util.List;

public class FollowReq extends ClientToServerMessage {

    private List<String> userNameList = new LinkedList<>();
    private boolean toFollow;

    public FollowReq(String decodedMessage){
        super(decodedMessage);
        init();
    }

    protected void init(){
        byte[] toFollowUnfollow = decodedMessage.substring(0, 1).getBytes();
        if(toFollowUnfollow[0] == 0)
            toFollow = true;
        else
            toFollow = false;
        decodedMessage = decodedMessage.substring(3);
        String[] users = decodedMessage.split("\0");
        for(int i = 0; i < users.length; i++)
            userNameList.add(users[i]);
    }

    public void execute(int connectionId, SystemData data, Connections connections, BidiMessagingProtocolImpl bidiMessagingProtocol){
        String userName = data.searchLoggedInUser(connectionId);
        List<String> succeedUsers = null;
        if(userName != null) {
            if (toFollow) {
                succeedUsers = data.followUsers(userNameList, userName);
            }
            else // to unFollow
                succeedUsers = data.unFollow(userNameList, userName);
        }
        if(succeedUsers != null && !succeedUsers.isEmpty()) {
            connections.send(connectionId, new AckMesFollow((short) 4,(short) succeedUsers.size(), succeedUsers));
        }
        else{
            connections.send(connectionId, new ErrorMes((short) 4));
        }


    }

}
