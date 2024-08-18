package netty.simpleNetty.simpleNettyCommunication;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * simple netty client
 */
@Slf4j
public class ClientNetty {
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        new Bootstrap()
                // 添加EventLoop
                .group(new NioEventLoopGroup())
                // 添加channel
                .channel(NioSocketChannel.class)
                // 添加处理器
                .handler(
                    new ChannelInitializer<NioSocketChannel>() {
                        // 连接建立后被调用
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            // 对发送的数据编码
                            ch.pipeline().addLast(new StringEncoder());
                        }
                    }
                )
                .connect(InetAddress.getLocalHost(), 8080)
                .sync()
                .channel()  // 获取channel通道,是抽象通道,可以进行数据读写操作
                .writeAndFlush(new Date() + ": Hello Server...");   // 向服务端发送数据,写入并清空缓存区
    }
}
