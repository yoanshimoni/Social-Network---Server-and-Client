package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.Server;
import bgu.spl.net.srv.SystemData;

public class ReactorMain {

    public static void main(String[] args) {
        SystemData  data = new SystemData(); //one shared object

        Server.reactor(
                Integer.parseInt((args[1])),Integer.parseInt(args[0]), //port and num of threads
                () -> new BidiMessagingProtocolImpl(data), //protocol factory
                MessageEncoderDecoderImpl::new //message encoder decoder factory
        ).serve();


    }
}
