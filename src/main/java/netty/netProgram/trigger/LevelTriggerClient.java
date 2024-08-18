package netty.netProgram.trigger;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

@Slf4j
public class LevelTriggerClient {
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                sb.append("a" + i % 26);
            }
            ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
            socketChannel.write(buffer);
            log.debug("client send data len is : {}", sb.toString().length());
            while (true){
                // 死循环，维持客户端和服务端的链接,使得服务端能够完全读取数据
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
