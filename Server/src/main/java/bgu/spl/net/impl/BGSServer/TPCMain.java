package bgu.spl.net.impl.BGSServer;


import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.Server;
import bgu.spl.net.srv.SystemData;


public class TPCMain {

    public static void main(String[] args) {
        SystemData data = new SystemData(); //one shared object

        Server.threadPerClient(
                Integer.parseInt(args[0]), //port
                () -> new BidiMessagingProtocolImpl(data), //protocol factory
                MessageEncoderDecoderImpl::new //message encoder decoder factory
        ).serve();


    }
}
