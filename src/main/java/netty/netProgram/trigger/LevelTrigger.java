package netty.netProgram.trigger;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * 验证Java NIO中的水平触发
 */
@Slf4j
public class LevelTrigger {
    public static void main(String[] args) throws IOException {
        server();
    }
    private static void server() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel severChannel = ServerSocketChannel.open();
        severChannel.configureBlocking(false);
        severChannel.bind(new InetSocketAddress(8888));
        System.out.println("Server start!");
        severChannel.register(selector, SelectionKey.OP_ACCEPT);
        int count = 0;
        //select会阻塞，知道有就绪连接写入selectionKeys
        while (!Thread.currentThread().isInterrupted()) {
            if (selector.select(100) == 0) {
                continue;
            }
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                //SelectionKey为select中记录的就绪请求的数据结构，其中包括了连接所属的socket及就绪的类型
                SelectionKey key = keys.next();
                //处理事件，不管是否可以处理完成，都删除 key。因为 soketChannel 为水平触发的，
                // 未处理完成的事件删除后会被再次通知
                keys.remove();
                if (key.isAcceptable()) {
                    System.out.println("触发连接事件");
                    SocketChannel socketChannel = severChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(8);
                    int len = socketChannel.read(byteBuffer);
                    byteBuffer.flip();
                    if (len == -1) {
                        key.cancel();
                        socketChannel.close();
                    }
                    count += len;
                    log.debug("current receive data len is : {}, total receive data len is :{}", len, count);
//                    if ( byteBuffer.remaining() > 0) {
//                        System.out.print(new String(getString(byteBuffer)));
//                    }
//                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
            }
        }
    }

    public static String getString(ByteBuffer buffer) {
        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        try {
            charset = StandardCharsets.UTF_8;
            decoder = charset.newDecoder();
            // charBuffer = decoder.decode(buffer);//用这个的话，只能输出来一次结果，第二次显示为空
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
