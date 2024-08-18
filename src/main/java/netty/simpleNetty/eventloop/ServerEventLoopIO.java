package netty.simpleNetty.eventloop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;


/**
 * 使用EventLoop处理IO事件的Server
 */
@Slf4j
public class ServerEventLoopIO {
    public static void main(String[] args) throws InterruptedException {
        new ServerBootstrap()
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buffer = msg instanceof ByteBuf ? ((ByteBuf) msg) : null;
                                if(buffer != null){
                                    byte[] bytes = new byte[16];
                                    ByteBuf len = buffer.readBytes(bytes, 0, buffer.readableBytes());
                                    log.info(new String(bytes));
                                }
                            }
                        });
                    }
                })
                .bind(8000)
                .sync();
    }
}
