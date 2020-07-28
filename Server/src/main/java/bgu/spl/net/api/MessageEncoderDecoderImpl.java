package bgu.spl.net.api;

import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.MessegesTypes.ClientToServerMessages.*;
import bgu.spl.net.api.bidi.MessegesTypes.MessageType;
import bgu.spl.net.api.bidi.MessegesTypes.ServerToClientMessages.*;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {

    private List<Byte> opBytes = new LinkedList<>();
    private List<Byte> msgBytes = new LinkedList<>();
    private boolean typeChecked = false;
    private MessageType type;
    private int numOfZeros;
    private int numOfSeenZeros = 0;

    @Override
    public Message decodeNextByte(byte nextByte) {
        if (!typeChecked) {
            opBytes.add(nextByte);
            if (opBytes.size() == 2) {
                //check the type of the message and init "type"
                checkType();
                if(type == MessageType.LOGOUT_REQ){
                    popString();
                    return new LogoutReq("");
                }
                else if(type == MessageType.USER_LIST_REQ){
                    popString();
                    return new UserListReq("");
                }
            }
            return null;

        } //if type already checked
        else {
            switch (type) {
                case REGISTER:
                    msgBytes.add(nextByte);
                    if (nextByte == 0)
                        numOfSeenZeros++;
                    if (numOfSeenZeros < numOfZeros)
                        return null;
                    else {
                        String decodedMessage = popString();
                        return new RegisterReq(decodedMessage);
                    }
                case LOGIN_REQ:
                    msgBytes.add(nextByte);
                    if (nextByte == 0)
                        numOfSeenZeros++;
                    if (numOfSeenZeros < numOfZeros)
                        return null;
                    else {
                        String decodedMessage = popString();
                        return new LoginReq(decodedMessage);
                    }

                case FOLLOW_REQ:
                    if(msgBytes.size() < 3) {
                        msgBytes.add(nextByte);
                        return null;
                    }
                    if(msgBytes.size() == 3) {
                        byte[] numOfUsers = new byte[2];
                        numOfUsers[0] = msgBytes.get(1);
                        numOfUsers[1] = msgBytes.get(2);
                        numOfZeros = (int) bytesToShort(numOfUsers);
                    }
                    if(numOfSeenZeros < numOfZeros){
                        msgBytes.add(nextByte);
                        if (nextByte == 0)
                            numOfSeenZeros++;
                        if(numOfSeenZeros == numOfZeros){
                            String decodedMessage = popString();
                            return new FollowReq(decodedMessage);
                        }
                        return null;
                    }

                case POST_REQ:
                    msgBytes.add(nextByte);
                    if(nextByte == 0){
                        String decodedMessage = popString();
                        return new PostReq(decodedMessage);
                    }
                    else
                        return null;

                case PM_REQ:
                    msgBytes.add(nextByte);
                    if (nextByte == 0)
                        numOfSeenZeros++;
                    if (numOfSeenZeros < numOfZeros)
                        return null;
                    else {
                        String decodedMessage = popString();
                        return new PMReq(decodedMessage);
                    }

                case STATS_REQ:
                    msgBytes.add(nextByte);
                    if(nextByte == 0){
                        String decodedMessage = popString();
                        return new StatsReq(decodedMessage);
                    }
                    else
                        return null;

            }
            return null;
        }

    }



    @Override
    public byte[] encode(Message message) {
        byte[] bytes;
        if(message instanceof NotificationMes){
            bytes = ((NotificationMes) message).toBytes();
        }
        else if(message instanceof AckMes){
            if(message instanceof AckMesFollow){
                bytes = ((AckMesFollow) message).toBytes();
            }
            else if(message instanceof AckMesStat){
                bytes = ((AckMesStat) message).toBytes();
            }
            else if(message instanceof AckMesUserList) {
                bytes = ((AckMesUserList) message).toBytes();
            }
            else{
                bytes = new byte[4];
                bytes[0] = shortToBytes((short) 10)[0];
                bytes[1] = shortToBytes((short) 10)[1];
                bytes[2] = shortToBytes((((AckMes) message).getMsgOpCode()))[0];
                bytes[3] = shortToBytes((((AckMes) message).getMsgOpCode()))[1];
            }

        }
        else{ // if(message instanceof ErrorMes)
            bytes = new byte[4];
            byte[] opcode = shortToBytes((short) 11);
            byte[] msgOpcode = shortToBytes(((ErrorMes) message).getMsgOpCode());
            bytes[0] = opcode[0];
            bytes[1] = opcode[1];
            bytes[2] = msgOpcode[0];
            bytes[3] = msgOpcode[1];
        }

        return bytes;

    }

    private void checkType(){
        short opcode = bytesToShort(listToBytes(opBytes));
        switch (opcode){
            case 1: type = MessageType.REGISTER;
                numOfZeros = 2;
                break;
            case 2: type = MessageType.LOGIN_REQ;
                numOfZeros = 2;
                break;
            case 3: type = MessageType.LOGOUT_REQ;
                numOfZeros = 0;
                break;
            case 4: type = MessageType.FOLLOW_REQ;
                break;
            case 5: type = MessageType.POST_REQ;
                numOfZeros = 1;
                break;
            case 6: type = MessageType.PM_REQ;
                numOfZeros = 2;
                break;
            case 7: type = MessageType.USER_LIST_REQ;
                numOfZeros = 0;
                break;
            case 8: type = MessageType.STATS_REQ;
                numOfZeros = 1;
                break;
        }
        typeChecked = true;
    }

    private byte[] listToBytes(List<Byte> list) {
        byte [] output = new byte[list.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = list.get(i);
        }
        return output;
    }

    private String popString() {
        byte [] message = listToBytes(msgBytes);
        String result = new String(message, 0, message.length, StandardCharsets.UTF_8);
        msgBytes.clear();
        opBytes.clear();
        typeChecked = false;
        type = null;
        numOfZeros = 0;
        numOfSeenZeros = 0;
        return result;
    }

    public short bytesToShort(byte[] byteArr) {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

}
