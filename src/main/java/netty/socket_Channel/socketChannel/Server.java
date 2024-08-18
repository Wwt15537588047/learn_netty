package netty.socket_Channel.socketChannel;

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
 * 网络编程，创建服务端
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        // 使用nio来理解阻塞模式，单线程
        // 构建ByteBuffer缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 绑定监听的端口
        ssc.bind(new InetSocketAddress(8080));
        // 创建连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while(true){
            // accept 建立与客户端的连接，SocketChannel用来与客户端之间通信
            log.info("connecting...");
            SocketChannel socketChannel = ssc.accept(); // 阻塞
            log.info("connected ... {}", socketChannel);
            channels.add(socketChannel);
            for (SocketChannel channel : channels) {
                // 接收客户端发送的数据
                log.info("before read ... {}", channel);
                channel.read(buffer);   // 阻塞
                buffer.flip();
                ByteBufferUtil.debugRead(buffer);
                buffer.clear();
                log.info("after read ... {}", channel);
            }
        }
    }
}