package netty.netProgram.nonblock;

import lombok.extern.slf4j.Slf4j;
import netty.utils.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * nonBlock Server code
 */
@Slf4j
public class Server {
    public static void main(String[] args) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(16);
            // 创建服务端ServerSocketChannel
            ServerSocketChannel ssc = ServerSocketChannel.open();
            // 设置为非阻塞
            ssc.configureBlocking(false);
            // 绑定端口
            ssc.bind(new InetSocketAddress(8080));
            List<SocketChannel> channels = new ArrayList<>();
            while (true){
                // 建立连接
//                log.debug("server connecting...");
                SocketChannel sc = ssc.accept();
                if(sc != null){
                    // 设置sc为非阻塞
                    sc.configureBlocking(false);
                    log.debug("server connected, sc : {}", sc);
                    channels.add(sc);
                }
                for (SocketChannel channel : channels) {
                    int read = channel.read(buffer);
                    if(read <= 0){
                        continue;
                    }
                    buffer.flip();
                    ByteBufferUtil.debugAll(buffer);
                    buffer.clear();
                    log.debug("receive data from client end, sc : {}", channel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
