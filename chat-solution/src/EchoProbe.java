import labnet.Frame;

import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoProbe {
    public static void main(String[] args) throws Exception {

        var host = InetAddress.getLoopbackAddress();
        try (var listener = new ServerSocket(0, 1, host);
             var sender = new Socket(host, listener.getLocalPort());
             var receiver = listener.accept()) {
            var request = new Frame(Frame.ECHO, 2, Frame.bytes("Chat"));
            sender.getOutputStream().write(request.encode().array());
            var received = Frame.read(new DataInputStream(receiver.getInputStream()));
            var reply = new Frame(Frame.DATA, received.id(), received.body());
            receiver.getOutputStream().write(reply.encode().array());
            var result = Frame.read(new DataInputStream(sender.getInputStream()));
            System.out.println(result.id() + " " + Frame.text(result.body()));
        }
    }
}
