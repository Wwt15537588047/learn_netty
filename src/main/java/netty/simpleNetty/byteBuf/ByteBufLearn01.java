package netty.simpleNetty.byteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import netty.utils.ByteBufUtil;

/**
 * Learn ByteBuf
 */
@Slf4j
public class ByteBufLearn01 {
    public static void main(String[] args) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(10);
        byteBuf.writeBytes(new byte[]{'1','2','3'});
        ByteBuf heapByteBuf = ByteBufAllocator.DEFAULT.heapBuffer(8);
        ByteBuf directByteBuf = ByteBufAllocator.DEFAULT.directBuffer(12);
        ByteBufUtil.log(byteBuf);
        ByteBufUtil.log(heapByteBuf);
        ByteBufUtil.log(directByteBuf);
    }
}
