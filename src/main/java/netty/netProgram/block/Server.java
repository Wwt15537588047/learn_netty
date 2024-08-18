package netty.netProgram.block;

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
 * 阻塞
 */
@Slf4j
public class Server {
    public static void main(String[] args) {
        try {
            // 创建buffer用于接收从客户端获取的数据
            ByteBuffer buffer = ByteBuffer.allocate(10);
            // 创建服务端channel
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 绑定连接的端口
            serverSocketChannel.bind(new InetSocketAddress(8080));
            // 连接集合
            List<SocketChannel> channels = new ArrayList<>();
            while (true){
                // 阻塞等待连接事件
                log.debug("connecting...");
                SocketChannel sc = serverSocketChannel.accept();
                log.debug("connected sc : {}", sc);
                channels.add(sc);
                for (SocketChannel channel : channels) {
                    // 接收客户端发送的数据
                    log.debug("receive data from client...");
                    int read = channel.read(buffer);
                    if(read == -1){
                        continue;
                    }
                    log.debug("receive data len is: {}", read);
                    // 切换为读模式
                    buffer.flip();
                    ByteBufferUtil.debugAll(buffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
