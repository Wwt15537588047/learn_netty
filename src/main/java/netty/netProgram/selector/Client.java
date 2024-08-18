package netty.netProgram.selector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * selector处理accept事件Server
 */
@Slf4j
public class Client {
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            log.debug("client is connecting...");
            socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8080));
            log.debug("client connected");
            ByteBuffer buffer = ByteBuffer.allocate(128);
            buffer.put("Hello Server i am client...".getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            while (true){}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
