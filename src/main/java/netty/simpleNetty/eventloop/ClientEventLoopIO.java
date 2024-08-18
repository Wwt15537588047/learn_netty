package netty.simpleNetty.eventloop;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * 使用EventLoop处理IO事件的Client
 */
@Slf4j
public class ClientEventLoopIO {
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        Channel channel = new Bootstrap()
                .group(new NioEventLoopGroup(3))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        log.info("init...");
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                    }
                })
                .connect(new InetSocketAddress(InetAddress.getLocalHost(), 8000))
                .sync()
                .channel();
//        channel.writeAndFlush(ByteBufAllocator.DEFAULT.buffer().writeBytes("zhangSan".getBytes()));
//        Thread.sleep(1000);
//        channel.writeAndFlush(ByteBufAllocator.DEFAULT.buffer().writeBytes("zhangSan".getBytes()));
//        channel.writeAndFlush(ByteBufAllocator.DEFAULT.buffer().writeBytes("liSi".getBytes()));
//        Thread.sleep(1000);
//        channel.writeAndFlush(ByteBufAllocator.DEFAULT.buffer().writeBytes("liSi".getBytes()));
        channel.writeAndFlush(ByteBufAllocator.DEFAULT.buffer().writeBytes("wangTao".getBytes()));
        Thread.sleep(1000);
        channel.writeAndFlush(ByteBufAllocator.DEFAULT.buffer().writeBytes("wangTao".getBytes()));
    }
}
