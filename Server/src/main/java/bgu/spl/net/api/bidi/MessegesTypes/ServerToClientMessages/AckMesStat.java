package bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages;

import java.util.List;

public class AckMesStat extends AckMes {

    private short numOfPosts;
    private short numOfFollowers;
    private short numOfFollowing;

    public AckMesStat(short messageOpcode, short numOfPosts, short numOfFollowers, short numOfFollowing) {
        super(messageOpcode);
        this.numOfPosts = numOfPosts;
        this.numOfFollowers = numOfFollowers;
        this.numOfFollowing = numOfFollowing;
    }

    public byte[] toBytes(){
        byte[] toSend = new byte[10];
        toSend[0] = shortToBytes((short) 10)[0];
        toSend[1] = shortToBytes((short) 10)[1];
        toSend[2] = shortToBytes( (short) 8)[0];
        toSend[3] = shortToBytes((short) 8)[1];
        toSend[4] = shortToBytes(numOfPosts)[0];
        toSend[5] = shortToBytes(numOfPosts)[1];
        toSend[6] = shortToBytes(numOfFollowers)[0];
        toSend[7] = shortToBytes(numOfFollowers)[1];
        toSend[8] = shortToBytes(numOfFollowing)[0];
        toSend[9] = shortToBytes(numOfFollowing)[1];
        return toSend;
    }


}
