package netty.netProgram.selector;

import lombok.extern.slf4j.Slf4j;
import netty.utils.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * selector Accept And Read Server
 */
@Slf4j
public class ServerAcceptRead {
    public static void main(String[] args) {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(8080));
            Selector selector = Selector.open();
            // 注册ssc到selector上,并且注明事件类型为ACCEPT事件
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while (true){
                int count = selector.select();
                if(count <= 0){
                    continue;
                }
                log.debug("current connect num : {}", count);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectionKeys.iterator();
                while (iter.hasNext()){
                    SelectionKey key = iter.next();
                    // 判断事件类型是accept还是read
                    if(key.isAcceptable()){
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel sc = serverSocketChannel.accept();
                        log.debug("ssc is: {}, sc is:{}", serverSocketChannel, sc);
                        sc.configureBlocking(false);
                        // 将sc注册到selector上,并设置其类型为Read
                        sc.register(selector, SelectionKey.OP_READ);
                    }else if(key.isReadable()){
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(128);
                        int read = sc.read(buffer);
                        if(read == -1){
                            // 此时client已经关闭连接
                            // key.cancel()取消注册在selector上的channel,并从keys集合中删除key后续不在监听事件
                            key.cancel();
                            sc.close();
                        }else{
                            buffer.flip();
                            ByteBufferUtil.debugAll(buffer);
                        }
                    }
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
