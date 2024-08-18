package netty.simpleNetty.simpleNettyCommunication;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 初入netty Learn Server
 */
@Slf4j
public class ServerNetty {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup()) // Group组
                .channel(NioServerSocketChannel.class)
                // boss负责处理链接，worker(child)负责处理读写，决定了worker(child)能执行哪些操作(handler)
                .childHandler(
                // channel负责和客户端进行数据传输的通道,Initializer初始化,负责添加别的handler
                new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 添加具体的handler
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            // 读事件
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
                                log.info("server receive msg:{}",msg);
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
