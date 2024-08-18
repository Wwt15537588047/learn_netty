package netty.netProgram.block;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * 阻塞Client代码编写
 */
@Slf4j
public class Client {
    public static void main(String[] args) {
        try {
            // 创建client socketChannel
            SocketChannel sc = SocketChannel.open();
            // 连接到localhost 8080端口
            sc.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8080));
            log.debug("client connected");
            while (true){
                // 死循环，防止代码退出
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
