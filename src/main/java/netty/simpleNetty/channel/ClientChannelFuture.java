package netty.simpleNetty.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * 客户端关闭channel异步以及回调函数实现
 */
@Slf4j
public class ClientChannelFuture {
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup(2))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {

                    }
                })
                .connect(new InetSocketAddress(InetAddress.getLocalHost(), 8000));
        log.info("Before sync : {}",channelFuture.channel());
//        channelFuture.sync();
//        log.info("After sync : {}", channelFuture.channel());
        channelFuture.addListener((ChannelFutureListener)future -> {
            log.info("After listen : {}", future.channel());
        });
//        channelFuture.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                log.info("After listen : {}", future.channel());
//            }
//        });
    }
}
