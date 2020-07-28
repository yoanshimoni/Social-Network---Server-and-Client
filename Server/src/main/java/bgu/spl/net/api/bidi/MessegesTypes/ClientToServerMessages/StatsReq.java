package bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.AckMesStat;
import bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessage;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.ErrorMes;
import bgu.spl.net.srv.SystemData;

public class StatsReq extends ClientToServerMessage {

    private String userNameToStat;

    public StatsReq(String decodedMessage) {
        super(decodedMessage);
        init();
    }


    protected void init() {
        userNameToStat = decodedMessage.substring(0, decodedMessage.length() - 1);

    }

    public void execute(int connectionId, SystemData data, Connections connections, BidiMessagingProtocolImpl bidiMessagingProtocol) {
        String userName = data.searchLoggedInUser(connectionId);
        if (userName != null && data.isRegistered(userNameToStat)) {
            short numOfPosts = data.numOfPosts(userNameToStat);
            short numOfFollowers = (short) data.whoFollowsMe(userNameToStat).size();
            short numOfFollowing = data.numOfFollowing(userNameToStat);
            connections.send(connectionId, new AckMesStat((short) 8, numOfPosts, numOfFollowers, numOfFollowing));
        }
        else{
            connections.send(connectionId, new ErrorMes((short) 8));
        }
    }
}

