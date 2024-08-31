package netty.mydesignprotocol.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import netty.mydesignprotocol.message.LoginRequestMessage;

/**
 * 测试自定义Message编解码器
 */
@Slf4j
public class TestMessageCodec {
    public static void main(String[] args) throws Exception {
        // LoggingHandler源码上面加@Sharable注解，可以在多个handler之间复用，不存在并发问题
        LoggingHandler LOGGING_HANDLER = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(
                LOGGING_HANDLER,
                // 自定义编解码器能否完美解决粘包问题，对于特殊场景下的半包问题仍然无法解决，此时可结合固定长度帧解码器进行解决
                // 自定义编解码器能在一定程度上解决粘包、半包问题，但不是完全解决粘包、半包问题
                new LengthFieldBasedFrameDecoder(1024,12,4,0,0),
                /**
                 * 接收缓冲区可能不足以容纳一个消息编码后的所有字节,此时会有半包（不存在粘包）,这时候如果应用层读取的话反序列化就失败,此时需要帧解码器
                 * 帧解码器的作用：防止半包时反序列化失败
                 */
                new MessageCodec()
        );
        // 自定义Message
        LoginRequestMessage loginMessage = new LoginRequestMessage("zhangsan", "123456");
        // 出栈,测试编码(encode)
//        channel.writeOutbound(loginMessage);

        // decode,测试解码，入栈解码，需要先将Message编码为ByteBuf,随后调用入栈处理器就会进行解码
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, loginMessage, byteBuf);
//        channel.writeInbound(byteBuf);

        // 验证半包情况下的反序列化失败
        ByteBuf buf1 = byteBuf.slice(0, 100);
        ByteBuf buf2 = byteBuf.slice(100, byteBuf.readableBytes() - 100);
        buf1.retain();  // retain()将底层对应的引用计数+1,调用channel.writeInbound()后会自动将ByteBuf对应的引用计数-1,此时不retain,后续此ByteBuf无法使用
        channel.writeInbound(buf1);
        channel.writeInbound(buf2);
    }
}
