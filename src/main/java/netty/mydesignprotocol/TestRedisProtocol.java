package netty.mydesignprotocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 测试Redis协议的使用,创建Redis客户端,连接已经启动的redis服务器,并向redis服务器发送指定内容,获取服务器返回结果
 * Windows下启动redis服务器,进入redis文件,打开终端,输入 redis-server.exe redis.windows.config启动redis服务器
 * set name zhangsan
 *  *  *3
 *  *  $3
 *  *  set
 *  *  $4
 *  *  name
 *  *  $8
 *  *  zhangsan
 */
@Slf4j
public class TestRedisProtocol {
    // 定义换行符
    private final static byte[] LINE = new byte[]{13, 10};
    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ByteBuf buf = ctx.alloc().buffer();
                            buf.writeBytes("auth 123456".getBytes()).writeBytes(LINE);
                            ctx.writeAndFlush(buf);

                            ByteBuf buffer = ctx.alloc().buffer();
                            //                        super.channelInactive(ctx);
                            buffer.writeBytes("*3".getBytes()).writeBytes(LINE);
                            buffer.writeBytes("$3".getBytes()).writeBytes(LINE);
                            buffer.writeBytes("set".getBytes()).writeBytes(LINE);
                            buffer.writeBytes("$4".getBytes()).writeBytes(LINE);
                            buffer.writeBytes("name".getBytes()).writeBytes(LINE);
                            buffer.writeBytes("$8".getBytes()).writeBytes(LINE);
                            buffer.writeBytes("zhangsan".getBytes()).writeBytes(LINE);
                            ctx.writeAndFlush(buffer);
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            System.out.println(buf.toString(Charset.defaultCharset()));
                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("localhost", 6379)).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }
}
