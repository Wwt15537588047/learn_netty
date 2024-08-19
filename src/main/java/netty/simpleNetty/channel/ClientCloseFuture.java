package netty.simpleNetty.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Test CloseFuture的异步以及回调实现
 */
@Slf4j
public class ClientCloseFuture {
    public static void main(String[] args) throws InterruptedException, UnknownHostException {
        Channel channel = new Bootstrap()
                .group(new NioEventLoopGroup(2))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress(InetAddress.getLocalHost(), 8000))
                .sync()
                .channel();
        log.info("channel : {}", channel);
        new Thread(()->{
            Scanner scanner = new Scanner(System.in);
            while (true){
                String line = scanner.nextLine();
                if("q".equals(line)){
                    // channel的close也是一个异步操作,本线程不会执行close操作,而是交由一个其他线程执行close操作
                    channel.close();
                    break;
                }
                channel.writeAndFlush(line);
            }
        }).start();
        ChannelFuture closeFuture = channel.closeFuture();
        log.info("waiting close...");
        closeFuture.sync();
        log.info("closed...");

        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("closed...");
            }
        });

//        channel.closeFuture().addListener((ChannelFutureListener) channelFuture1 ->{
//                    log.info("closed...");
//        });
    }
}
