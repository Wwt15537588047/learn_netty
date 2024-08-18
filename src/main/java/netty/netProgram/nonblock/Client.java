package netty.netProgram.nonblock;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 非阻塞Client编写
 */
@Slf4j
public class Client {
    public static void main(String[] args) {
        try {
            // 创建client socketChannel
            SocketChannel sc = SocketChannel.open();
            // 连接localhost 8080
            sc.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8080));
            log.debug("non block client connected server.");
            ByteBuffer buffer = ByteBuffer.allocate(16);
            while (true){
                // 死循环防止代码退出
                buffer.put("Hello Client".getBytes());
                buffer.flip();
                sc.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
