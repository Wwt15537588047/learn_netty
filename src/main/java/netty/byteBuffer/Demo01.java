package netty.byteBuffer;


import lombok.extern.slf4j.Slf4j;
import netty.utils.ByteBufferUtil;
import java.nio.ByteBuffer;

@Slf4j
public class Demo01 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put("test 01".getBytes());
        buffer.flip();
        int limit = buffer.limit();
        int capacity = buffer.capacity();
        int position = buffer.position();
        log.info("position is {}, capacity is {}, limit is {}", position, capacity, limit);
        ByteBufferUtil.debugAll(buffer);
    }
}