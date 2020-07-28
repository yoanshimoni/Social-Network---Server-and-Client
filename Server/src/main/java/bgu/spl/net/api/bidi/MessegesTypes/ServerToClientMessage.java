package bgu.spl.net.api.bidi.MessegesTypes;

import bgu.spl.net.api.bidi.Message;

public abstract class ServerToClientMessage extends Message {

    private short messageOpcode;

    public ServerToClientMessage(short messageOpcode){
        this.messageOpcode = messageOpcode;
    }

    public short getMsgOpCode(){
        return messageOpcode;
    }

    protected static byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    public abstract byte[] toBytes();
}
