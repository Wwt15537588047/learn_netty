package netty.socket_Channel.socketChannel;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Slf4j
public class Client {
    public static void main(String[] args) {
        try {

            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8080));
            ByteBuffer byteBuffer = ByteBuffer.allocate(16);
            byteBuffer.put("Hello Netty".getBytes());
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
